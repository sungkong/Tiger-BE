package com.tiger.service;

import com.tiger.domain.CommonResponseDto;
import com.tiger.domain.bank.Bank;
import com.tiger.domain.member.Member;
import com.tiger.domain.order.Orders;
import com.tiger.domain.order.Status;
import com.tiger.domain.order.dto.*;
import com.tiger.domain.payment.PayMethod;
import com.tiger.domain.payment.Payment;
import com.tiger.domain.vehicle.Vehicle;
import com.tiger.exception.CustomException;
import com.tiger.repository.*;
import com.tiger.utils.CheckUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.tiger.exception.StatusCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final CheckUtil checkUtil;
    private final OrderRepository orderRepository;
    private final OrderCustomRepository orderCustomRepository;
    private final PaymentRepository paymentRepository;
    private final BankRepository bankRepository;

    // 주문하기
    @Transactional
    public CommonResponseDto<?> order(HttpServletRequest request, Long vehicleId, OrderRequestDto orderRequestDto){

        // 회원 검증
        Member member = checkUtil.validateMember();
        // 상품 검증
        Vehicle vehicle = checkUtil.validateVehicle(vehicleId);
        // 예약 기간 검증
        checkUtil.validateOrderDate(vehicleId, orderRequestDto);
        // 주문 금액 검증
        checkUtil.validatePrice(orderRequestDto, vehicle);
        // 본인 상품 주문 블락
        checkUtil.validateSelfOrder(member, vehicle);
        // Dto -> Domain
        Orders order = Orders.builder()
                .member(member)
                .startDate(orderRequestDto.getStartDate())
                .endDate(orderRequestDto.getEndDate())
                .status(Status.RESERVED)
                .vehicle(vehicle)
                .totalAmount(orderRequestDto.getPaidAmount())
                .build();


        Long order_id = orderRepository.save(order).getId();

        if(order_id == null){
            return CommonResponseDto.fail(ORDER_NOT_FOUND);
        } else {
                PayMethod payMethod = null;
            try { // 결제수단 검증
                payMethod = PayMethod.valueOf(orderRequestDto.getPayMethod().toUpperCase());
            } catch (IllegalArgumentException e) {
                return CommonResponseDto.fail(PAYMETHOD_NOT_FOUND);
            }

            paymentRepository.save(Payment.builder()
                    .order(order)
                    .paidAmount(orderRequestDto.getPaidAmount())
                    .payMethod(payMethod)
                    .build());
            Bank bank = checkUtil.validateBank(order);
            bank.deposit((long) orderRequestDto.getPaidAmount());
            return CommonResponseDto.success(ORDER_SUCCESS, null);
        }
    }

    // 주문 리스트(렌터)
    @Transactional(readOnly = true)
    public CommonResponseDto<?> getOrderListRenter(HttpServletRequest request, String status, int limit, int offset) {

        // 회원 검증
        Member member = checkUtil.validateMember();

        // status 검증
        try {
            Status.valueOf(status);
        } catch (IllegalArgumentException e) {
            return CommonResponseDto.fail(STATUS_NOT_FOUND);
        }
        List<Orders> ordersList =  orderRepository.getOrderList(member.getId(), status, limit, offset).orElse(null);

        return CommonResponseDto.success(ORDERLIST_SUCCESS ,ordersList.stream().map(
                order -> new OrderResponseDto(order)).collect(Collectors.toList()));
    }

    @Transactional(readOnly = true)
    public  List<String> getReservedDateList(Long vid){
        List<Orders> findOrdersList = orderRepository.findAllByVehicleIdAndStatusNotOrderByStartDateAsc(vid,Status.CANCEL).orElseThrow(() -> new CustomException(VEHICLE_NOT_FOUND));

        List<LocalDate> dtoList = new ArrayList<>();

        for (Orders findOrder : findOrdersList) {

            LocalDate startDate = findOrder.getStartDate();
            LocalDate endDate = findOrder.getEndDate();
            if (startDate.isEqual(endDate)) {
                dtoList.add(startDate);
            } else {
                dtoList.addAll(startDate.datesUntil(endDate.plusDays(1))
                        .collect(Collectors.toList()));

            }
        }


        List<String> stringList = new ArrayList<>();
       for (LocalDate revDate : dtoList){
           String reservedDate = revDate.format(DateTimeFormatter.ofPattern("yyyy-M-d"));
           stringList.add(reservedDate);
       }
        return stringList;

    }


    // 환불(렌터)
    @Transactional
    public CommonResponseDto<?> refund(HttpServletRequest request, Long orderId) {

        Orders order = checkUtil.validateOrder(orderId);
        Member member = checkUtil.validateMember();
        // 주문 번호 존재 검증
        // 로그인 유저가 주문한 것인지 확인하는 검증
        checkUtil.validateOwner(member, order);
        // 환불 가능한 상태 검증
        List<Payment> payments = paymentRepository.findAllByOrderId(order.getId()).orElseThrow(
                () -> new CustomException(PAYMENT_NOT_FOUND)
        );
        Long price = 0l;
        for (Payment payment : payments) {
            price += payment.getPaidAmount();
        }
        Bank bank =bankRepository.findById(1L).orElseThrow(
                () -> new CustomException(BANK_NOT_FOUND)
        );
        if(bank.getMoney() - price < 0){
            throw new CustomException(EXCESS_AMOUNT_BANK);
        }

        LocalDate now = LocalDate.now();
        if(now.compareTo(order.getStartDate()) >= 0 || !order.getStatus().toString().equals("RESERVED")){
            throw new CustomException(REFUND_ELIGIBILITY_NOT_FOUND);
        }
        // Bank에서 돈 빠져나감
        bank.withdraw(price);
        // Orders 환불 상태로 변경
        order.setStatus(Status.CANCEL);
        // 상품 사용기간 재오픈(필요한가?)
        return CommonResponseDto.success(CANCLE_SUCCESS, null);
    }

    // 반납 확인
    @Transactional
    public CommonResponseDto<?> returnVehicle(HttpServletRequest request, Long orderId){


        // 주문 번호 존재 검증
        Orders order = checkUtil.validateOrder(orderId);
        // 로그인 유저와 오너가 동일한지 검증
        Member member = checkUtil.validateMember();
        checkUtil.validateOwner(member, order);
        // 조기 반납인 경우 남은 기간 재오픈

        // 반납완료로 상태 변경하기
        order.setReturn();
        return CommonResponseDto.success(RETURN_SUCCESS, null);
    }

    //판매리스트 (오너)
    @Transactional(readOnly = true)
    public CommonResponseDto<?> getOrderListOwner(HttpServletRequest request, String status, int limit, int offset) {

        /*
        * 렌터와 코드는 똑같으나, 추후 렌터의 주문리스트와 오너의 주문리스트에 차이를 둘 수도 있을 것 같아 분리함
        * 마지막 리팩토링할 떄 똑같으면, 하나로 합치자.
        * */

        // 멤버검증
        Member member = checkUtil.validateMember();
        // status 검증
        try {
            Status.valueOf(status);
        } catch (IllegalArgumentException e) {
            return CommonResponseDto.fail(STATUS_NOT_FOUND);
        }

        List<OrderResponseDto> ordersList =  orderCustomRepository.getOrderListOwner(member.getId(), status, limit, offset);

        return CommonResponseDto.success(ORDERLIST_SUCCESS, ordersList);
    }

    // 일일 매출
    @Transactional(readOnly = true)
    public CommonResponseDto<?> getIncomeListDay(LocalDate date) {

        // 멤버검증
        Member owner = checkUtil.validateMember();
        Map<String, Object> resultMap = new HashMap<>();
        // 라인그래프
        resultMap.put("Line", orderCustomRepository.getIncomeListDay(owner.getId(), date));
        // 파이그래프
        resultMap.put("pie", orderCustomRepository.getIncomeListDayPie(owner.getId(), date));
        // 바 그래프
        resultMap.put("bar", orderCustomRepository.getIncomeListDayBar(owner.getId(), date));

        return CommonResponseDto.success(INCOMELIST_SUCCESS, resultMap);

    }
    // 월 매출 (날짜+매출만)
    @Transactional(readOnly = true)
    public CommonResponseDto<?> getIncomeListMonth(LocalDate date) {

        // 멤버검증
        Member owner = checkUtil.validateMember();
        Map<String, Object> resultMap = new HashMap<>();
        // 라인그래프
        resultMap.put("Line", orderCustomRepository.getIncomeListMonth(owner.getId(), date));
        // 파이그래프
        resultMap.put("pie", orderCustomRepository.getIncomeListMonthPie(owner.getId(), date));
        // 바 그래프
        resultMap.put("bar", orderCustomRepository.getIncomeListMonthBar(owner.getId(), date));

        return CommonResponseDto.success(INCOMELIST_SUCCESS, resultMap);
    }


    /**
     * 매일 정오 예약 당일이 된 상품 status = use로 변경하는 스케줄링
     * 추후 + 알림까지 추가하여 배치로 적용할 예정
     * 매일 24시 0분 1초에 실행
     * */
    @Scheduled(cron = "1 0 0 * * *", zone="Asia/Seoul")
    @Transactional
    public void changeStatusUse(){
        List<Orders> list = orderRepository.findAllByStartDateEquals(LocalDate.now()).get();
        for(int i=0; i<list.size(); i++){
            Orders order = list.get(i);
            order.setStatus(Status.USE);
            log.info("주문상태 USE로 변경 : {} ", order.getId());
        }
    }


}

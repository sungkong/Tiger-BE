package com.tiger.utils;

import com.tiger.domain.UserDetailsImpl;
import com.tiger.domain.bank.Bank;
import com.tiger.domain.member.Member;
import com.tiger.domain.openDate.OpenDate;
import com.tiger.domain.order.Orders;
import com.tiger.domain.order.Status;
import com.tiger.domain.order.dto.OrderRequestDto;
import com.tiger.domain.payment.Payment;
import com.tiger.domain.vehicle.Vehicle;
import com.tiger.exception.CustomException;
import com.tiger.exception.StatusCode;
import com.tiger.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static com.tiger.exception.StatusCode.*;

@Component
@RequiredArgsConstructor
public class CheckUtil {


    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final VehicleRepository vehicleRepository;
    private final BankRepository bankRepository;
    private final PaymentRepository paymentRepository;
    private final OpenDateRepository openDateRepository;

    // 회원 검증
    @Transactional(readOnly = true)
    public Member validateMember() {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal == null){
            throw new CustomException(USER_NOT_FOUND);
        }
        UserDetails userDetails = (UserDetails) principal;
        return ((UserDetailsImpl) userDetails).getMember();
//        return memberRepository.findById(memberId).orElseThrow(
//                () -> new CustomException(USER_NOT_FOUND)
//        );
    }

    // 주문번호 검증
    @Transactional(readOnly = true)
    public Orders validateOrder(long orderId) {
        return orderRepository.findById(orderId).orElseThrow(
                () -> new CustomException(INVALID_AUTH_ORDER)
        );
    }

    // 상품 검증
    @Transactional(readOnly = true)
    public Vehicle validateVehicle(long vehicleId) {
        return vehicleRepository.findByIdAndIsValid(vehicleId, true).orElseThrow(
                () -> new CustomException(VEHICLE_NOT_FOUND)
        );
    }

    // 로그인 계정과 상품주인 확인
    @Transactional(readOnly = true)
    public void validateOwner(Member member, Orders order){
        if(!order.getMember().getId().equals(member.getId())){
            throw new CustomException(HOST_NOT_FOUND);
        }
    }

    // 본인 상품 주문 블락
    @Transactional(readOnly = true)
    public void validateSelfOrder(Member member, Vehicle vehicle){
        if(member.getId().equals(vehicle.getOwnerId())){
            throw new CustomException(SELF_ORDER_FORBIDDEN);
        }
    }

    // 주문 금액 검증
    @Transactional(readOnly = true)
    public void validatePrice(OrderRequestDto orderRequestDto, Vehicle vehicle){
        if(orderRequestDto.getPaidAmount() !=
                vehicle.getPrice()*(orderRequestDto.getEndDate().compareTo(orderRequestDto.getStartDate())+1)){
            throw new CustomException(StatusCode.PRICE_NOT_FOUND);
        }
    }

    /**
     * 손성우 2022.9.7.
     * 예약 기간 검증
     * @param vehicleId
     * @param orderRequestDto
     */
    @Transactional(readOnly = true)
    public void validateOrderDate(Long vehicleId, OrderRequestDto orderRequestDto){

        // 예약 당일 이전 날짜들 예약 막아놓기
        LocalDate now = LocalDate.now();
        if(now.compareTo(orderRequestDto.getStartDate()) > 0){
            throw new CustomException(EXPIRED_DATE_FORBIDDEN);
        }
        // 주문기간만 포함된 달의 openDate만 가져오기
        List<OpenDate> openDateList = openDateRepository.findAllByIncludeOrderDateMonth(vehicleId,
                orderRequestDto.getStartDate(),
                orderRequestDto.getEndDate()).orElseThrow(
                () -> new CustomException(OPENDATE_NOT_FOUND)
        );
        // 상품의 오픈 기간 가져오기
        Set<LocalDate> openDateSet = new HashSet<>();
        for (OpenDate openDate : openDateList) {
            LocalDate startDate = openDate.getStartDate();
            int i=0;
            while (i<=openDate.getStartDate().until(openDate.getEndDate(), ChronoUnit.DAYS)){
                openDateSet.add(startDate.plusDays(i++));
            }
        }

        // 오픈 기간에서 이미 사용중인 기간 제외하기
        List<Orders> ordersList = orderRepository.findAllByVehicleIdAndStatusNot(vehicleId,Status.CANCEL).orElse(null);
        for (Orders order : ordersList) {
            int j=0;
            while (j<=order.getStartDate().until(order.getEndDate(), ChronoUnit.DAYS)){
                openDateSet.remove(order.getStartDate().plusDays(j++));
            }
        }
        // 최종 검증
        int k=0;
        while(k <= orderRequestDto.getEndDate().compareTo(orderRequestDto.getStartDate())){
            if(!openDateSet.contains(orderRequestDto.getStartDate().plusDays(k++))){
                throw new CustomException(DUPLICATE_ORDERDATE);
            }
        }
    }

    /**
     * 손성우 2022.9.4.
     * 계좌 검증
     * @param order
     * @return bank
     */
     @Transactional(readOnly = true)
     public Bank validateBank(Orders order){
         Bank bank =bankRepository.findById(1L).orElseThrow(
                 () -> new CustomException(BANK_NOT_FOUND)
         );
         return bank;
     }

    /**
     * 손성우 2022.9.7.
     * 환불 검증
     * @param order
     * @return bank
    */
     @Transactional(readOnly = true)
     public Bank validateReturn(Orders order){

         List<Payment> payments = paymentRepository.findAllByOrderId(order.getId()).orElseThrow(
                 () -> new CustomException(PAYMENT_NOT_FOUND)
         );
         Long sum = 0l;
         for (Payment payment : payments) {
             sum += payment.getPaidAmount();
         }
         Bank bank = validateBank(order);
         if(bank.getMoney() - sum < 0){
             throw new CustomException(EXCESS_AMOUNT_BANK);
         }

         LocalDate now = LocalDate.now();
         if(now.compareTo(order.getStartDate()) >= 0 || !order.getStatus().toString().equals("RESERVED")){
             throw new CustomException(REFUND_ELIGIBILITY_NOT_FOUND);
         }
         return bank;
     }


}

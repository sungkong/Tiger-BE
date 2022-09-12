package com.tiger.utils;

import com.tiger.domain.bank.Bank;
import com.tiger.domain.openDate.OpenDate;
import com.tiger.domain.order.Orders;
import com.tiger.domain.order.dto.OrderRequestDto;
import com.tiger.domain.payment.PayMethod;
import com.tiger.domain.payment.Payment;
import com.tiger.domain.vehicle.Vehicle;
import com.tiger.exception.CustomException;
import com.tiger.repository.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.tiger.exception.StatusCode.OPENDATE_NOT_FOUND;
import static com.tiger.exception.StatusCode.PRICE_NOT_FOUND;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CheckUtilTest {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OpenDateRepository openDateRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private BankRepository bankRepository;


    @Test
    @DisplayName("주문 금액 검증")
    void validatePrice(){
        // given

        Vehicle vehicle = vehicleRepository.findByIdAndIsValid(1L, true).get();

        String imp_uid = "0003"; // 가맹점에서 생성/관리하는 고유 주문번호
        String pay_method = PayMethod.CARD.toString(); // 결제 수단
        int paid_amount = 10000; // 결제 금액
        LocalDate start_date = LocalDate.parse("2022-09-04"); // 시작 날짜
        LocalDate end_date = LocalDate.parse("2022-09-13"); // 종료 날짜
        // when

        OrderRequestDto orderRequestDto = new OrderRequestDto(
                imp_uid, pay_method, paid_amount, start_date, end_date);
        // then
        assertThat(vehicle.getPrice()*(orderRequestDto.getEndDate().compareTo(orderRequestDto.getStartDate())+1))
                .isEqualTo(orderRequestDto.getPaidAmount());


    }

    @Test
    @DisplayName("예약 기간 검증")
    void validatePeriod(){

        // given

        HttpServletRequest request = null;
        Long vehicleId = 3l;

        String imp_uid = "0011"; // 가맹점에서 생성/관리하는 고유 주문번호
        String pay_method = PayMethod.CARD.toString(); // 결제 수단
        int paid_amount = 20000; // 결제 금액
        LocalDate start_date = LocalDate.parse("2022-09-20"); // 시작 날짜
        LocalDate end_date = LocalDate.parse("2022-10-05"); // 종료 날짜
        OrderRequestDto orderRequestDto = new OrderRequestDto(
                imp_uid, pay_method, paid_amount, start_date, end_date);
        boolean flag = true;

        // 상품의 오픈 기간인지 체크
        // 10.1~10.5 , 10.11~10.15
//        List<OpenDate> openDateList = openDateRepository.findAllByVehicleIdOrderByStartDate(vehicleId).orElseThrow(
//                () -> new CustomException(OPENDATE_NOT_FOUND)
//        );
        List<OpenDate> openDateList = openDateRepository.findAllByIncludeOrderDateMonth(vehicleId,
                orderRequestDto.getStartDate(),
                orderRequestDto.getEndDate()).orElseThrow(
                () -> new CustomException(OPENDATE_NOT_FOUND)
        );
        Set<LocalDate> openDateSet = new HashSet<>();
        for (OpenDate openDate : openDateList) {
            LocalDate startDate = openDate.getStartDate();
            openDateSet.add(openDate.getStartDate());
            int i=0;
            //System.out.println("size = "+size);
            while (i<=openDate.getEndDate().compareTo(openDate.getStartDate())){
                openDateSet.add(startDate.plusDays(i++));
            }
        }

        // 오픈 기간에서 이미 사용중인 기간 제외하기
        List<Orders> ordersList = orderRepository.findAllByVehicleId(vehicleId).orElse(null);
        for (Orders order : ordersList) {
            int j=0;
            int size = order.getEndDate().compareTo(order.getStartDate());
            while (j<=size){
                openDateSet.remove(order.getStartDate().plusDays(j++));
            }
        }
        // 검증
        int k=0;
        while(k <= orderRequestDto.getEndDate().compareTo(orderRequestDto.getStartDate())){
            if(!openDateSet.contains(orderRequestDto.getStartDate().plusDays(k++))){
                flag = false;
                break;
            }
        }
        for(LocalDate date : openDateSet){
            System.out.println("예약 가능 날짜 = " + date);
        }
        //assertThat(openDateSet.size()).isEqualTo(32);
        assertThat(flag).isFalse();
        //assertThat(flag).isTrue();

    }

    @Test
    @DisplayName("환불 기간 검증")
    void validateReturnPeriod(){
        boolean result = true;
        LocalDate now = LocalDate.now();
        Orders order = orderRepository.findById(1l).get();
        if(now.compareTo(order.getStartDate()) >= 0 && !order.getStatus().equals("RESERVED")){
            result = false;
        }
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("출금 가능한지 검증")
    void validateBank(){
        boolean result = true;
        //given
        List<Payment> payments = paymentRepository.findAllByOrderId(1l).get();
        // when
        Long sum = 0l;
        for (Payment payment : payments) {
            sum += payment.getPaidAmount();
        }
        Bank bank =bankRepository.findById(1L).orElseThrow(
                () -> new IllegalArgumentException("계좌가 존재하지 않습니다.")
        );
        if(bank.getMoney() - sum < 0){
            result = false;
            // throw new CustomException(PRICE_NOT_FOUND);
        }
        // then
        System.out.println(sum);
        assertThat(result).isTrue();
    }


}
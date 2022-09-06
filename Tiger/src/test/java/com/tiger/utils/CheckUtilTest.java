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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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

        // 상품의 오픈 기간인지 체크
        List<OpenDate> allByVehicleId = openDateRepository.findAllByVehicleId(1L).get();
        for (OpenDate openDate : allByVehicleId) {
            System.out.println(openDate);
        }
        // 주문 리스트에서 검증
       // List<Orders> ordersList = orderRepository.findAllByVehicleId(1L).get();


        //Vehicle vehicle = vehicleRepository.findByIdAndIsValid(1L, true).get();

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
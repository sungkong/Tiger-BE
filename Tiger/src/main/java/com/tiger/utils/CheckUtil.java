package com.tiger.utils;

import com.tiger.domain.CommonResponseDto;
import com.tiger.domain.bank.Bank;
import com.tiger.domain.member.Member;
import com.tiger.domain.order.Orders;
import com.tiger.domain.order.dto.OrderRequestDto;
import com.tiger.domain.payment.Payment;
import com.tiger.domain.vehicle.Vehicle;
import com.tiger.exception.CustomException;
import com.tiger.exception.StatusCode;
import com.tiger.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tiger.exception.StatusCode.*;

@Component
@RequiredArgsConstructor
public class CheckUtil {


    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final VehicleRepository vehicleRepository;
    private final BankRepository bankRepository;
    private final PaymentRepository paymentRepository;

    // 회원 검증
    @Transactional(readOnly = true)
    public Member validateMember(long memberId) {
        return memberRepository.findById(memberId).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND)
        );
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
            throw new CustomException(StatusCode.HOST_NOT_FOUND);
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

     // 출금 가능한지 검증
     @Transactional(readOnly = true)
     public void validateBank(Orders order){

         List<Payment> payments = paymentRepository.findAllByOrderId(order.getId()).orElseThrow(
                 () -> new CustomException(PAYMENT_NOT_FOUND)
         );
         Long sum = 0l;
         for (Payment payment : payments) {
             sum += payment.getPaidAmount();
         }
         Bank bank =bankRepository.findById(1L).orElseThrow(
                 () -> new CustomException(BANK_NOT_FOUND)
         );
         if(bank.getMoney() - sum < 0){
             throw new CustomException(EXCESS_AMOUNT_BANK);
         }
     }

     // 환불 기간 검증
     @Transactional(readOnly = true)
     public void validateReturn(Orders order){

         LocalDate now = LocalDate.now();
         if(now.compareTo(order.getStartDate()) >= 0 || !order.getStatus().equals("RESERVED")){
             throw new CustomException(REFUND_ELIGIBILITY_NOT_FOUND);
         }
     }


}

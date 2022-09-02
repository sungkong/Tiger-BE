package com.tiger.service;

import com.tiger.domain.member.Member;
import com.tiger.domain.order.Orders;
import com.tiger.domain.order.Status;
import com.tiger.domain.order.dto.OrderRequestDto;
import com.tiger.domain.payment.PayMethod;
import com.tiger.domain.payment.Payment;
import com.tiger.domain.vehicle.Vehicle;
import com.tiger.repository.OrderRepository;
import com.tiger.repository.PaymentRepository;
import com.tiger.utils.CheckUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CheckUtil checkUtil;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    // 주문하기
    @Transactional
    public ResponseEntity<?> order(HttpServletRequest request, Long vehicleId, OrderRequestDto orderRequestDto){

        // 회원 검증
        Member member = checkUtil.validateMember(1L);
        // 상품 검증
        Vehicle vehicle =null;
        // 예약 기간 검증
        // 주문 금액 검증

        // Dto -> Domain
        Orders orders = Orders.builder()
                .member(member)
                .start_date(orderRequestDto.getStart_date())
                .end_date(orderRequestDto.getEnd_date())
                .status(Status.RESERVED)
                .build();


        Long order_id = orderRepository.save(orders).getId();
        if(order_id == null){
            return ResponseEntity.badRequest().body(Map.of("msg", "결제를 실패했습니다."));
        } else {
                PayMethod payMethod = null;
            try { // 결제수단 검증
                payMethod = PayMethod.valueOf(orderRequestDto.getPay_method());
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body(Map.of("msg", "결제 수단이 잘못 입력되었습니다."));
            }

            paymentRepository.save(Payment.builder()
                    .orders(orders)
                    .paid_amount(orderRequestDto.getPaid_amount())
                    .pay_method(payMethod)
                    .build());
            return ResponseEntity.ok("결제 성공");
        }
    }

    // 주문 리스트(렌터)
    @Transactional(readOnly = true)
    public ResponseEntity<?> getOrderListRenter(HttpServletRequest request, Long renterId) {

        // 회원 검증
        Member member = checkUtil.validateMember(1L);
        return ResponseEntity.ok(orderRepository.findAllByMemberId(member.getId()).orElse(null));

    }

    // 환불(렌터)
    public ResponseEntity<?> refund(HttpServletRequest request, Long orderId) {

        // 주문 번호 존재 검증
        Orders order = checkUtil.validateOrder(orderId);
        // 로그인 유저가 주문한 것인지 확인하는 검증
        // 환불 가능한 상태 검증
        // 정산 계좌(bank) 에서 돈 입출이 가능한지 검증

        // Orders 환불 상태로 변경
        // Bank에서 돈 빠져나감
        // 상품 사용기간 재오픈

        return ResponseEntity.ok("굿");
    }

    // 반납 확인
    public ResponseEntity<?> returnCheck(HttpServletRequest request){

        // 주문 번호 존재 검증
        // 로그인 유저와 오너가 동일한지 검증
        // 조기 반납인 경우 남은 기간 재오픈
        // 반납완료로 상태 변경하기

        return ResponseEntity.ok("굿");
    }

}

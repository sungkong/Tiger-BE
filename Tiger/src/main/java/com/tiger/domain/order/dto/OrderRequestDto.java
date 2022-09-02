package com.tiger.domain.order.dto;

import com.tiger.domain.payment.PayMethod;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class OrderRequestDto {

    private String imp_uid; // 가맹점에서 생성/관리하는 고유 주문번호
    private String pay_method; // 결제 수단
    private int paid_amount; // 결제 금액
    private LocalDate start_date; // 시작 날짜
    private LocalDate end_date; // 종료 날짜
}

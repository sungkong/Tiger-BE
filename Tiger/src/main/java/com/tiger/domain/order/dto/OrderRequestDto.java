package com.tiger.domain.order.dto;

import com.tiger.domain.payment.PayMethod;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDto {

    private String impUid; // 가맹점에서 생성/관리하는 고유 주문번호
    private String payMethod; // 결제 수단
    private int paidAmount; // 결제 금액
    private LocalDate startDate; // 시작 날짜
    private LocalDate endDate; // 종료 날짜
}

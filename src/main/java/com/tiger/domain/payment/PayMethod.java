package com.tiger.domain.payment;

import lombok.Getter;

@Getter
public enum PayMethod {

    CARD, //(신용카드)
    TRANS, //(실시간계좌이체)
    VBANK, //(가상계좌)
    PHONE, //(휴대폰소액결제)
    SAMSUNG, //(삼성페이 / 이니시스, KCP 전용)
    KPAY, //(KPay앱 직접호출 / 이니시스 전용)
    KAKAOPAY, //(카카오페이 직접호출 / 이니시스, KCP, 나이스페이먼츠 전용)
    PAYCO,//(페이코 직접호출 / 이니시스, KCP 전용)
    LPAY, //(LPAY 직접호출 / 이니시스 전용)
    SSGPAY, //(SSG페이 직접호출 / 이니시스 전용)
    TOSSPAY, //(토스간편결제 직접호출 / 이니시스 전용)
    CULTURELAND, //(문화상품권 / 이니시스, 토스페이먼츠(구 LG U+), KCP 전용)
    SMARTCULTURE, //(스마트문상 / 이니시스, 토스페이먼츠(구 LG U+), KCP 전용)
    HAPPYMONEY, //(해피머니 / 이니시스, KCP 전용)
    BOOKNLIFE, //(도서문화상품권 / 토스페이먼츠(구 LG U+), KCP 전용)
    POINT, //(베네피아 포인트 등 포인트 결제 / KCP 전용)
    WECHAT, //(위쳇페이 / 엑심베이 전용)
    ALIPAY, //(알리페이 / 엑심베이 전용)
    UNIONPAY, //(유니온페이 / 엑심베이 전용)
    TENPAY, //(텐페이 / 엑심베이 전용)

}

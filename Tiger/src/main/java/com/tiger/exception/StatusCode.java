package com.tiger.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum StatusCode {

    /* 200 : 요청 성공 */
    SUCCESS(HttpStatus.OK, "요청에 성공하였습니다."),
    SCHEDULE_SUCCESS(HttpStatus.OK, "차량 스케줄링에 성공하였습니다."),
    ORDER_SUCCESS(HttpStatus.OK, "주문이 완료되었습니다."),
    ORDERLIST_SUCCESS(HttpStatus.OK, "주문목록을 가져왔습니다."),
    RETURN_SUCCESS(HttpStatus.OK, "반납이 완료되었습니다."),
    CANCLE_SUCCESS(HttpStatus.OK, "환불이 완료되었습니다."),
    INCOMELIST_SUCCESS(HttpStatus.OK, "수익현황을 가져왔습니다."),

    /* 400 BAD_REQUEST : 잘못된 요청 */

    /* 401 UNAUTHORIZED : 인증되지 않은 사용자 */
    INVALID_AUTH_TOKEN(HttpStatus.UNAUTHORIZED, "권한 정보가 없는 토큰입니다."),
    INVALID_AUTH_ORDER(HttpStatus.UNAUTHORIZED, "주문을 한 사용자만 접근할 수 있습니다."),

    /* 403 FORBIDDEN : 컨텐츠에 접근할 권리가 없음 */

    /* 404 NOT_FOUND : Resource를 찾을 수 없음 */
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 정보의 사용자를 찾을 수 없습니다."),
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 주분번호 입니다."),
    VEHICLE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 차량 입니다."),
    EXCESS_AMOUNT_BANK(HttpStatus.NOT_FOUND, "환불금액이 저축된 금액보다 커서 환불을 할 수 없습니다."),
    PRICE_NOT_FOUND(HttpStatus.NOT_FOUND, "주문금액이 적절하지 않습니다."),
    STATUS_NOT_FOUND(HttpStatus.NOT_FOUND, "적절하지 않은 주문상태 입니다."),
    PAYMETHOD_NOT_FOUND(HttpStatus.NOT_FOUND, "적절하지 않은 결제수단 입니다."),
    HOST_NOT_FOUND(HttpStatus.NOT_FOUND, "주문을 한 사용자와 로그인 계정과 일치하지 않습니다."),
    REFUND_ELIGIBILITY_NOT_FOUND(HttpStatus.NOT_FOUND, "환불을 할 수 없습니다."),
    BANK_NOT_FOUND(HttpStatus.NOT_FOUND, "계좌를 찾을 수 없습니다."),
    PAYMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "결제수단을 찾을 수 없습니다."),
    OPENDATE_NOT_FOUND(HttpStatus.NOT_FOUND, "사용기간이 존재하지 않는 상품입니다."),


    /* 409 : CONFLICT : Resource의 현재 상태와 충돌. 보통 중복된 데이터 존재 */
    DUPLICATE_RESOURCE(HttpStatus.CONFLICT, "데이터가 이미 존재합니다."),
    DUPLICATE_ORDERDATE(HttpStatus.CONFLICT, "이미 사용중인 상품이거나 사용할 수 없는 상품입니다."),


    ;

    private final HttpStatus httpStatus;
    private final String message;
}

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
    GET_SCHEDULE_SUCCESS(HttpStatus.OK, "차량 스케줄링 조회에 성공하였습니다."),
    ORDER_SUCCESS(HttpStatus.OK, "주문이 완료되었습니다."),
    ORDERLIST_SUCCESS(HttpStatus.OK, "주문목록을 가져왔습니다."),
    RETURN_SUCCESS(HttpStatus.OK, "반납이 완료되었습니다."),
    CANCLE_SUCCESS(HttpStatus.OK, "환불이 완료되었습니다."),
    INCOMELIST_SUCCESS(HttpStatus.OK, "수익현황을 가져왔습니다."),
    USER_CREATED(HttpStatus.CREATED, "회원가입에 성공하셨습니다."),
    LOGIN_SUCCESS(HttpStatus.OK, "로그인에 성공하셨습니다."),
    LOGOUT_SUCCESS(HttpStatus.OK, "로그아웃에 성공하셨습니다."),
    USABLE_EMAIL(HttpStatus.OK, "사용 가능한 이메일입니다."),
    TOKEN_REISSUED(HttpStatus.OK, "토큰이 재발급되었습니다."),
    VEHICLE_CREATED(HttpStatus.CREATED, "차량 등록에 성공하셨습니다."),
    VEHICLE_UPDATED(HttpStatus.OK, "차량 정보 수정에 성공하셨습니다."),
    VEHICLE_DELETED(HttpStatus.OK, "차량 삭제에 성공하셨습니다."),
    HEART_SUCCESS(HttpStatus.OK, "좋아요 성공"),
    HEART_DELETED(HttpStatus.OK, "좋아요 취소 성공"),
    HEARTLIST_SUCCESS(HttpStatus.OK, "좋아요목록 가져오기 성공"),

    REVIEW_SUCCESS(HttpStatus.OK, "리뷰 작성 성공"),
    REVIEW_UPDATED(HttpStatus.OK, "리뷰 수정 성공"),
    REVIEW_LIST_SUCCESS(HttpStatus.OK, "리뷰 조회 성공"),

    REVIEW_FIRST(HttpStatus.OK, "리뷰 최초 등록"),

    REVIEW_DELETED(HttpStatus.OK, "리뷰 삭제 성공"),
    THUMBNAIL_UPDATED(HttpStatus.OK, "썸네일이 수정 성공"),







    /* 400 BAD_REQUEST : 잘못된 요청 */
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "유효하지 않은 비밀번호입니다."),
    INVALID_EMAIL(HttpStatus.BAD_REQUEST, "유효하지 않은 이메일입니다."),

    INVALID_DATE(HttpStatus.BAD_REQUEST, "유효하지 않은 날짜입니다."),
    BAD_IMAGE_INPUT(HttpStatus.BAD_REQUEST, "이미지 파일 형식을 지켜주세요"),
    CANNOT_CHAT_BY_ONESELF(HttpStatus.BAD_REQUEST, "자기 자신과 채팅을 할 수는 없습니다."),

    /* 401 UNAUTHORIZED : 인증되지 않은 사용자 */
    INVALID_AUTH_ORDER(HttpStatus.UNAUTHORIZED, "주문을 한 사용자만 접근할 수 있습니다."),
    INVALID_AUTH_UPDATE(HttpStatus.UNAUTHORIZED, "허용된 사용자만 수정할 수 있습니다."),
    EMAIL_ALREADY_EXISTS(HttpStatus.UNAUTHORIZED, "이미 존재하는 이메일입니다."),
    INVALID_TOKEN_SIGNATURE(HttpStatus.UNAUTHORIZED, "유효하지 않은 JWT 서명 입니다"),
    EXPIRED_AUTH_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 JWT 토큰 입니다"),
    UNSUPPORTED_AUTH_TOKEN(HttpStatus.UNAUTHORIZED, "지원되지 않는 JWT 토큰 입니다"),
    INVALID_AUTH_TOKEN(HttpStatus.UNAUTHORIZED, "잘못된 JWT 토큰 입니다"),
    INVALID_CHATROOM_EXIT(HttpStatus.UNAUTHORIZED, "잘못된 방식으로 채팅방을 나갔습니다."),
    INVALID_RETURN_STATUS(HttpStatus.UNAUTHORIZED, "사용중인 상품이 아니라 반납을 할 수 없습니다."),

    /* 403 FORBIDDEN : 컨텐츠에 접근할 권리가 없음 */
    EXPIRED_DATE_FORBIDDEN(HttpStatus.FORBIDDEN, "이전 날짜들을 예약할 수 없습니다."),
    SELF_ORDER_FORBIDDEN(HttpStatus.FORBIDDEN, "본인 상품은 주문할 수 없습니다."),

    REVIEW_NO_MORE(HttpStatus.FORBIDDEN, "리뷰는 1회에 한해 작성할 수 있습니다."),


    /* 404 NOT_FOUND : Resource를 찾을 수 없음 */
    USERNAME_NOT_FOUND(HttpStatus.NOT_FOUND, "로그인된 사용자만 접근할 수 있습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 정보의 사용자를 찾을 수 없습니다."),
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 주분번호 입니다."),
    DATE_NOT_FOUND(HttpStatus.NOT_FOUND,"등록되지 않은 DATE입니다."),
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
    VALIDATION_FAILURE(HttpStatus.NOT_FOUND, "입력값이 올바르지 않습니다."),

    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND,"존재하지 않는 리뷰입니다."),


    /* 409 : CONFLICT : Resource의 현재 상태와 충돌. 보통 중복된 데이터 존재 */
    DUPLICATE_RESOURCE(HttpStatus.CONFLICT, "데이터가 이미 존재합니다."),
    DUPLICATE_ORDERDATE(HttpStatus.CONFLICT, "이미 사용중인 상품이거나 사용할 수 없는 상품입니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}

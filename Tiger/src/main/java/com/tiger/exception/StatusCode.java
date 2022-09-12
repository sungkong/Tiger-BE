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

    USER_CREATED(HttpStatus.CREATED, "회원가입에 성공하셨습니다."),
    LOGIN_SUCCESS(HttpStatus.OK, "로그인에 성공하셨습니다."),
    LOGOUT_SUCCESS(HttpStatus.OK, "로그아웃에 성공하셨습니다."),
    USABLE_EMAIL(HttpStatus.OK, "사용 가능한 이메일입니다."),
    TOKEN_REISSUED(HttpStatus.OK, "토큰이 재발급되었습니다."),


    VEHICLE_CREATED(HttpStatus.CREATED, "차량 등록에 성공하셨습니다."),
    VEHICLE_UPDATED(HttpStatus.OK, "차량 정보 수정에 성공하셨습니다."),
    VEHICLE_DELETED(HttpStatus.OK, "차량 삭제에 성공하셨습니다."),


    /* 400 BAD_REQUEST : 잘못된 요청 */
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "유효하지 않은 비밀번호입니다."),
    INVALID_EMAIL(HttpStatus.BAD_REQUEST, "유효하지 않은 이메일입니다."),

    /* 401 UNAUTHORIZED : 인증되지 않은 사용자 */
    INVALID_AUTH_ORDER(HttpStatus.UNAUTHORIZED, "주문을 한 사용자만 접근할 수 있습니다."),
    INVALID_AUTH_UPDATE(HttpStatus.UNAUTHORIZED, "허용된 사용자만 수정할 수 있습니다."),
    EMAIL_ALREADY_EXISTS(HttpStatus.UNAUTHORIZED, "이미 존재하는 이메일입니다."),
    INVALID_TOKEN_SIGNATURE(HttpStatus.UNAUTHORIZED, "유효하지 않은 JWT 서명 입니다"),
    EXPIRED_AUTH_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 JWT 토큰 입니다"),
    UNSUPPORTED_AUTH_TOKEN(HttpStatus.UNAUTHORIZED, "지원되지 않는 JWT 토큰 입니다"),
    INVALID_AUTH_TOKEN(HttpStatus.UNAUTHORIZED, "잘못된 JWT 토큰 입니다"),

    /* 404 NOT_FOUND : Resource를 찾을 수 없음 */
    USERNAME_NOT_FOUND(HttpStatus.NOT_FOUND, "로그인된 사용자만 접근할 수 있습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 정보의 사용자를 찾을 수 없습니다."),
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 주분번호 입니다."),
    VEHICLE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 차량 입니다."),
    EXCESS_AMOUNT_BANK(HttpStatus.NOT_FOUND, "환불금액이 저축된 금액보다 커서 환불을 할 수 없습니다."),
    PRICE_NOT_FOUND(HttpStatus.NOT_FOUND, "주문금액이 적절하지 않습니다."),
    STATUS_NOT_FOUND(HttpStatus.NOT_FOUND, "적절하지 않은 주문상태 입니다."),

    /* 409 : CONFLICT : Resource의 현재 상태와 충돌. 보통 중복된 데이터 존재 */
    DUPLICATE_RESOURCE(HttpStatus.CONFLICT, "데이터가 이미 존재합니다."),


    ;

    private final HttpStatus httpStatus;
    private final String message;
}
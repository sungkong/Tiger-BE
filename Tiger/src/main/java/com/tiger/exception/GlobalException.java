package com.tiger.exception;

import com.tiger.domain.CommonResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalException {

    /*@ExceptionHandler(CustomException.class)
    protected ResponseEntity<ErrorResponseEntity> handleCustomException(CustomException e) {
        return ErrorResponseEntity.toResponseEntity(e.getStatusCode());
    }*/

    @ExceptionHandler(CustomException.class)
    protected CommonResponseDto<?> handleCustomException(CustomException e) {
        return CommonResponseDto.fail(e.getStatusCode());
    }
}

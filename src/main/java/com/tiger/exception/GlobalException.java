package com.tiger.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ErrorResponseEntity> handleCustomException(CustomException e) {
        return ErrorResponseEntity.toResponseEntity(e.getStatusCode());
    }

//    @ExceptionHandler(NumberFormatException.class)
//    public ResponseDto<?> handleNumberFormatException(){
//        return ResponseDto.fail("NumberFormatException","잘못된 요청입니다. ");
//    }
}

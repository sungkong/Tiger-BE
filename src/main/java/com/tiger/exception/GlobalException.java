package com.tiger.exception;

import com.tiger.domain.CommonResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.tiger.exception.StatusCode.*;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(CustomException.class)
    protected CommonResponseDto<?> handleCustomException(CustomException e) {
        return CommonResponseDto.fail(e.getStatusCode());
    }

    @ExceptionHandler(NumberFormatException.class)
    public CommonResponseDto<?> handleNumberFormatException(){
        return CommonResponseDto.fail(NUMBER_FORMAT_EXCEPTION);
    }

}

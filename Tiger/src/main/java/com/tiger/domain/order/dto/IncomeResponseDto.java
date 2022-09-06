package com.tiger.domain.order.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@NoArgsConstructor
public class IncomeResponseDto {

    private String date; // 날짜
    private int sum; // 합
    @QueryProjection
    public IncomeResponseDto(String date, int sum) {
        this.date = date;
        this.sum = sum;
    }
}

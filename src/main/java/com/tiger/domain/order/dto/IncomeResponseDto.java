package com.tiger.domain.order.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

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


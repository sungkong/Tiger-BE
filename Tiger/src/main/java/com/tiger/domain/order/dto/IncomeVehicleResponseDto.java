package com.tiger.domain.order.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class IncomeVehicleResponseDto {

    private Long vid;
    private String vbrand;
    private String vname;
    private int sum; // 합
    private String date; // 날짜

    @QueryProjection
    public IncomeVehicleResponseDto(Long vid, String vbrand, String vname, int sum, String date) {
        this.vid = vid;
        this.vbrand = vbrand;
        this.vname = vname;
        this.sum = sum;
        this.date = date;
    }
}


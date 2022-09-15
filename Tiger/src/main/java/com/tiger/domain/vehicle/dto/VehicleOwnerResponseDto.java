package com.tiger.domain.vehicle.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Builder
@Getter
@Setter
public class VehicleOwnerResponseDto {

    private Long vid;
    private String thumbnail;
    private String vbrand;
    private String vname;
    private Integer price;
    private String location;
    private String createdAt;
}
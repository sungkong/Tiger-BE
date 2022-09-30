package com.tiger.domain.vehicle.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
public class VehicleSearch {

    @NotNull(message = "시작일을 입력해주세요.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @NotNull(message = "종료일을 입력해주세요.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @NotBlank(message = "주소를 입력해주세요.")
    private String location;

    @NotNull(message = "위도값을 입력해주세요.")
    private Double locationX;

    @NotNull(message = "경도값을 입력해주세요.")
    private Double locationY;

    @NotBlank(message = "차량 종류를 입력해주세요.")
    private String type;
}

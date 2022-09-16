package com.tiger.domain.vehicle.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class VehicleSearchResponseDto {

    // 상품 식별번호
    private Long vid;

    // 상품 주인
    private Long ownerId;

    // 상품 가격
    private Integer price;

    // 상품 설명
    private String description;

    // 상품 위치
    private String location;

    // 상품 위치: 위도
    private Double locationX;

    // 상품 위치: 경도
    private Double locationY;

    // 상품 이미지
    private String thumbnail;

    // 차 브랜드
    private String vbrand;

    // 차 이름
    private String vname;

    // 차 타입(세단, 쿠페, 왜건, SUV, 해치백, 리무진, 밴, 픽업트럭)
    private String type;

    // 차 연식
    private String years;

    // 차 연료 타입(휘발유, 경유, LPG, 전기, 수소)
    private String fuelType;

    // 차 탑승객 수
    private String passengers;

    // 차 변속기 타입(자동, 수동)
    private String transmission;

    // 차 연비
    private String fuelEfficiency;


    private LocalDate startDate;

    private LocalDate endDate;


    public VehicleSearchResponseDto(VehicleCustomResponseDto vehicleCustomResponseDto, LocalDate startDate, LocalDate endDate) {
        this.vid = vehicleCustomResponseDto.getVid();
        this.ownerId = vehicleCustomResponseDto.getOwnerId();
        this.price = vehicleCustomResponseDto.getPrice();
        this.description = vehicleCustomResponseDto.getDescription();
        this.location = vehicleCustomResponseDto.getLocation();
        this.locationX = vehicleCustomResponseDto.getLocationX();
        this.locationY = vehicleCustomResponseDto.getLocationY();
        this.thumbnail = vehicleCustomResponseDto.getThumbnail();
        this.vbrand = vehicleCustomResponseDto.getVbrand();
        this.vname = vehicleCustomResponseDto.getVname();
        this.type = vehicleCustomResponseDto.getType();
        this.years = vehicleCustomResponseDto.getYears();
        this.fuelType = vehicleCustomResponseDto.getFuelType();
        this.passengers = vehicleCustomResponseDto.getPassengers();
        this.transmission = vehicleCustomResponseDto.getTransmission();
        this.fuelEfficiency = vehicleCustomResponseDto.getFuelEfficiency();
        this.startDate = startDate;
        this.endDate = endDate;
    }


}

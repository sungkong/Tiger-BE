package com.tiger.domain.vehicle.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class VehicleCustomResponseDto {

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


    @QueryProjection
    public VehicleCustomResponseDto(Long vid, Long ownerId, Integer price, String description, String location, Double locationX, Double locationY, String thumbnail, String vbrand, String vname, String type, String years, String fuelType, String passengers, String transmission, String fuelEfficiency) {
        this.vid = vid;
        this.ownerId = ownerId;
        this.price = price;
        this.description = description;
        this.location = location;
        this.locationX = locationX;
        this.locationY = locationY;
        this.thumbnail = thumbnail;
        this.vbrand = vbrand;
        this.vname = vname;
        this.type = type;
        this.years = years;
        this.fuelType = fuelType;
        this.passengers = passengers;
        this.transmission = transmission;
        this.fuelEfficiency = fuelEfficiency;
    }
}

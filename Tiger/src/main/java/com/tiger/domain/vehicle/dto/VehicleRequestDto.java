package com.tiger.domain.vehicle.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VehicleRequestDto {

    // 상품 주인
    private Long ownerId;

    // 상품 가격
    private Integer price;

    // 상품 설명
    private String description;

    // 상품 위치
    private String address;

    // 상품 대표 이미지


    // 차 이름(브랜드 + 모델명)
    private String name;

    // 차 타입(세단, 쿠페, 왜건, SUV, 해치백, 리무진, 밴, 픽업트럭)
    private String type;

    // 차 연식
    private String years;

    // 차 연료 타입(휘발유, 경유, LPG, 전기, 수소)
    private String fuelType;

    // 차 변속기 타입(자동, 수동)
    private String transType;

    // 차 연비
    private String fuelEfficiency;

}

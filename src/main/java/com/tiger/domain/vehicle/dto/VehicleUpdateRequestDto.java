package com.tiger.domain.vehicle.dto;


import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class VehicleUpdateRequestDto {

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

    // 상품 추가 이미지 리스트
    private List<MultipartFile> imageList;

    // 삭제할 이미지 리스트
    private List<String> removeList;

    // 차 브랜드
    private String vbrand;

    // 차 이름
    private String vname;

    // 차 타입(경형, 중형, 대형, 승합RV, 수입)
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


}

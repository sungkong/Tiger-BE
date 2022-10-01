package com.tiger.domain.vehicle.dto;


import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import java.util.List;

@Getter
@Setter
public class VehicleUpdateRequestDto {

    // 상품 가격
    @Min(value = 1, message = "최소 가격은 1원입니다.")
    @Max(value = 1000000, message = "최대 가격은 100만원입니다.")
    @NotNull(message = "가격을 적어주세요.")
    private Integer price;

    // 상품 설명
    private String description;

    // 상품 위치
    @NotBlank(message = "주소를 입력해주세요.")
    private String location;

    // 상품 위치: 위도
    @NotNull(message = "위도값이 들어오지 않습니다.")
    private Double locationX;

    // 상품 위치: 경도
    @NotNull(message = "경도값이 들어오지 않습니다.")
    private Double locationY;

    // 상품 추가 이미지 리스트
    private List<MultipartFile> imageList;

    // 삭제할 이미지 리스트
    private List<String> removeList;

    // 차 브랜드
    @NotBlank(message = "차량 브랜드 이름을 입력해주세요.")
    private String vbrand;

    // 차 이름
    @NotBlank(message = "차량 모델명을 입력해주세요.")
    private String vname;

    // 차 타입(경형, 중형, 대형, 승합RV, 수입)
    @NotBlank(message = "차량의 종류를 입력해주세요.")
    private String type;

    // 차 연식
    @Min(value = 1990, message = "1990년 이전의 차량은 등록하지 못합니다.")
    @NotBlank(message = "차량의 연식을 입력해주세요.")
    private String years;

    // 차 연료 타입(휘발유, 경유, LPG, 전기, 수소)
    @NotBlank(message = "차량의 연료 종류를 입력해주세요.")
    private String fuelType;

    // 차 탑승객 수
    @Min(value = 1, message = "차량 탑습인원은 최소 1명이어야 합니다.")
    @NotBlank(message = "차량의 탑승 가능 인원을 입력해주세요.")
    private String passengers;

    // 차 변속기 타입(자동, 수동)
    @NotBlank(message = "차량의 변속기 종류를 입력해주세요.")
    private String transmission;

    // 차 연비
    @Min(value = 1, message = "차량의 연비는 최소 1이어야 합니다.")
    @NotBlank(message = "차량의 연비를 입력해주세요.")
    private String fuelEfficiency;

}

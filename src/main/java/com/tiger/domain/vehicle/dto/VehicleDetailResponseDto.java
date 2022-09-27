package com.tiger.domain.vehicle.dto;

import com.tiger.domain.member.Member;
import com.tiger.domain.vehicle.Vehicle;
import com.tiger.domain.vehicle.VehicleImage;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class VehicleDetailResponseDto {

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
    private List<String> imageList;

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


    // 오너 이름
    private String oname;

    // 오너 이메일
    private String email;

    // 오너 전화번호
    private String tel;

    // 오너 프로필 사진
    private String profileImage;

    // 예약 시작일
    private LocalDate startDate;

    // 예약 종료일
    private LocalDate endDate;

    private Long averageRating;

    public VehicleDetailResponseDto(Vehicle vehicle, Member member, LocalDate startDate, LocalDate endDate) {
        this.vid = vehicle.getId();
        this.ownerId = vehicle.getOwnerId();
        this.price = vehicle.getPrice();
        this.description = vehicle.getDescription();
        this.location = vehicle.getLocation();
        this.locationX = vehicle.getLocationX();
        this.locationY = vehicle.getLocationY();
        this.imageList = vehicle.getImages().stream().map(VehicleImage::getImageUrl).collect(Collectors.toList());
        this.vbrand = vehicle.getVbrand();
        this.vname = vehicle.getVname();
        this.type = vehicle.getType();
        this.years = vehicle.getYears();
        this.fuelType = vehicle.getFuelType();
        this.passengers = vehicle.getPassengers();
        this.transmission = vehicle.getTransmission();
        this.fuelEfficiency = vehicle.getFuelEfficiency();
        this.oname = member.getName();
        this.email = member.getEmail();
        this.tel = member.getTel();
        this.profileImage = member.getProfileImage();
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // 좋아요 여부
    private boolean isHeart = false;
    public void setHeart(boolean heart) {
        isHeart = heart;
    }
}

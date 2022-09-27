package com.tiger.domain.vehicle;


import com.tiger.domain.Timestamped;
import com.tiger.domain.order.Orders;
import com.tiger.domain.vehicle.dto.VehicleRequestDto;
import com.tiger.domain.vehicle.dto.VehicleUpdateRequestDto;
import com.tiger.domain.vehicle.heart.Heart;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class Vehicle extends Timestamped {

    // 상품 식별번호
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 상품 주인
    @Column(nullable = false)
    private Long ownerId;

    // 상품 가격
    @Column(nullable = false)
    private Integer price;

    // 상품 설명
    @Lob
    @Column(nullable = false)
    private String description;

    // 상품 주소
    @Column(nullable = false)
    private String location;

    // 상품 주소: 위도
    @Column(nullable = false)
    private Double locationX;

    // 상품 주소: 경도
    @Column(nullable = false)
    private Double locationY;


    // 상품 활성/비활성
    @Column(nullable = false)
    private Boolean isValid;

    // 상품 이미지
    @OneToMany(mappedBy = "vehicle", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VehicleImage> images;

    // 상품 썸네일
    @Column(nullable = false)
    private String thumbnail;

    /*
    ==============================================
     */

    // 차 브랜드
    @Column(nullable = false)
    private String vbrand;

    // 차 이름
    @Column(nullable = false)
    private String vname;


    // 차 타입(경형, 중형, 대형, 승합RV, 수입)
    @Column(nullable = false)
    private String type;

    // 차 연식
    @Column(nullable = false)
    private String years;

    // 차 연료 타입(휘발유, 경유, LPG, 전기, 수소)
    @Column(nullable = false)
    private String fuelType;

    // 차 탑승객 수
    @Column(nullable = false)
    private String passengers;

    // 차 변속기 타입(자동, 수동)
    @Column(nullable = false)
    private String transmission;

    // 차 연비
    @Column(nullable = false)
    private String fuelEfficiency;

    // 주문목록
    @OneToMany(mappedBy = "vehicle", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Orders> orders;



    public void update(VehicleUpdateRequestDto requestDto, Long ownerId) {
        this.ownerId = ownerId;
        this.price = requestDto.getPrice();
        this.description = requestDto.getDescription();
        this.location = requestDto.getLocation();
        this.vbrand = requestDto.getVbrand();
        this.vname = requestDto.getVname();
        this.type = requestDto.getType();
        this.years = requestDto.getYears();
        this.fuelType = requestDto.getFuelType();
        this.passengers = requestDto.getPassengers();
        this.transmission = requestDto.getTransmission();
        this.fuelEfficiency = requestDto.getFuelEfficiency();
    }

    public void delete(String defaultThumbnail) {
        this.thumbnail = defaultThumbnail;
        this.isValid = false;
    }

    // 좋아요 목록
    @OneToMany(mappedBy = "vehicle", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Heart> heartList;

}

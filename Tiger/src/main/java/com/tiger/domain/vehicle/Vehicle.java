package com.tiger.domain.vehicle;


import com.tiger.domain.Timestamped;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
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
    @Column(name = "owner_id", nullable = false)
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
    private String address;

    // 상품 활성/비활성
    @Column(nullable = false)
    private Boolean isValid;

    // 상품 대표 이미지

    /*
    ==============================================
     */

    // 차 이름(브랜드 + 모델명)
    @Column(nullable = false)
    private String name;

    // 차 타입(경형, 중형, 대형, 승합RV, 수입)
    @Column(nullable = false)
    private String type;

    // 차 연식
    @Column(nullable = false)
    private String years;

    // 차 연료 타입(휘발유, 경유, LPG, 전기, 수소)
    @Column(nullable = false)
    private String fuelType;

    // 차 변속기 타입(자동, 수동)
    @Column(nullable = false)
    private String transType;

    // 차 연비
    @Column(nullable = false)
    private String fuelEfficiency;

    // OpenDate 테이블과 연관관계

    // Order????

}

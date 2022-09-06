package com.tiger.domain.order.dto;

import com.tiger.domain.order.Orders;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDto {

    private Long oid; // 주문 식별번호
    private Long vid; // 상품 식별번호
    private String vname; // 상품 이름
    private int price; // 가격
    private String thumbnail; // 썸네일
    private String location; // 위치
    private LocalDate startDate; // 시작 날짜
    private LocalDate endDate; // 종료 날짜
    private LocalDateTime createdAt; // 등록 날짜

    public OrderResponseDto(Orders orders){
        this.oid = orders.getId();
        this.vid = orders.getVehicle().getId();
        this.vname = orders.getVehicle().getName();
        this.price = orders.getVehicle().getPrice();
        this.thumbnail = orders.getVehicle().getThumbnail();
        this.location = orders.getVehicle().getAddress();
        this.startDate = orders.getStartDate();
        this.endDate = orders.getEndDate();
        this.createdAt = orders.getCreatedAt();
    }

}

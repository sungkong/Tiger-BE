package com.tiger.repository;

import com.querydsl.core.types.Expression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tiger.domain.openDate.QOpenDate;
import com.tiger.domain.order.QOrders;
import com.tiger.domain.order.Status;
import com.tiger.domain.vehicle.QVehicle;
import com.tiger.domain.vehicle.dto.QVehicleCustomResponseDto;
import com.tiger.domain.vehicle.dto.VehicleCustomResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class VehicleCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;
    QVehicle vehicle = QVehicle.vehicle;
    QOrders orders = QOrders.orders;
    QOpenDate openDate = QOpenDate.openDate;

    //상품 검색
    public List<VehicleCustomResponseDto> searchVehicle(LocalDate startDate, LocalDate endDate, Double locationX, Double locationY, String type) {

        return jpaQueryFactory.select(new QVehicleCustomResponseDto(vehicle.id, vehicle.ownerId, vehicle.price, vehicle.description, vehicle.location, vehicle.locationX, vehicle.locationY, vehicle.thumbnail, vehicle.vbrand, vehicle.vname, vehicle.type, vehicle.years, vehicle.fuelType, vehicle.passengers, vehicle.transmission, vehicle.fuelEfficiency))
                .from(vehicle)
                .where(vehicle.type.eq(type)
                        .and(vehicle.isValid.eq(true)
                        .and(vehicle.locationX.between((locationX-0.2), (locationX+0.2)).and(vehicle.locationY.between((locationY-0.2), (locationY+0.2)))
                        .and(vehicle.id.in(
                                JPAExpressions.select(openDate.vehicle.id)
                                        .from(openDate)
                                        .where(openDate.startDate.loe(startDate)
                                                .and(openDate.endDate.goe(endDate)
                                                        .and(openDate.vehicle.id.notIn(
                                                                        JPAExpressions.select(orders.vehicle.id)
                                                                                .from(orders)
                                                                                .where(orders.startDate.between(startDate, endDate)
                                                                                        .or(orders.endDate.between(startDate, endDate)
                                                                                                .and(orders.startDate.loe(startDate)
                                                                                                .and(orders.endDate.goe(endDate)
                                                                                                        .and(orders.status.ne(Status.valueOf("CANCEL")))
                                                                                                )))

                                                                                )

                                                                )
                                                )))

                                        )
                        )))

                )
                .fetch();
    }


}

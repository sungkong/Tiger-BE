package com.tiger.repository;

import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tiger.domain.order.QOrders;
import com.tiger.domain.order.Status;
import com.tiger.domain.order.dto.IncomeResponseDto;
import com.tiger.domain.order.dto.OrderResponseDto;
import com.tiger.domain.order.dto.QIncomeResponseDto;
import com.tiger.domain.order.dto.QOrderResponseDto;
import com.tiger.domain.payment.QPayment;
import com.tiger.domain.vehicle.QVehicle;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;
    QOrders order = QOrders.orders;
    QVehicle vehicle = QVehicle.vehicle;

    // 판매리스트(오너)
    public List<OrderResponseDto> getOrderListOwner(Long ownerId, String status, int limit, int offset) {
        return jpaQueryFactory.select(new QOrderResponseDto(order.id, vehicle.id, vehicle.vname, vehicle.vbrand, order.totalAmount,
                vehicle.thumbnail, vehicle.location, order.startDate, order.endDate, order.createdAt, vehicle.ownerId))
                .from(order)
                .join(order.vehicle, vehicle)
                .where(vehicle.ownerId.eq(ownerId)
                    .and(order.status.eq(Status.valueOf(status))))
                .limit(limit)
                .offset(offset)
                .fetch();
    }


    // 수익현황(일별매출 / 기준 : 당일 월 )
    public List<IncomeResponseDto> getIncomeListDay(Long ownerId, LocalDate now) {
        return jpaQueryFactory.selectDistinct(new QIncomeResponseDto(
                dateFormatYYYYmmdd(),
                order.totalAmount.sum()))
                .from(order)
                .join(vehicle)
                .on(order.vehicle.id.eq(vehicle.id))
                .where(vehicle.ownerId.eq(ownerId)
                        .and(dateFormatYYYYmm(null).eq(dateFormatYYYYmm(now)))
                        .and(order.status.ne(Status.CANCEL)))
                .groupBy(dateFormatYYYYmmdd())
                .fetch();
    }
    // 수익현황( 월매출 / 기준 : 당일 연도 )
    public List<IncomeResponseDto> getIncomeListMonth(Long ownerId, LocalDate now) {
        return jpaQueryFactory.selectDistinct(new QIncomeResponseDto(
                dateFormatYYYYmm(null),
                order.totalAmount.sum()))
                .from(order)
                .join(vehicle)
                .on(order.vehicle.id.eq(vehicle.id))
                .where(vehicle.ownerId.eq(ownerId)
                        .and(dateFormatYYYY(null).eq(dateFormatYYYY(now)))
                        .and(order.status.ne(Status.CANCEL)))
                .groupBy(dateFormatYYYYmm(null))
                .fetch();
    }

    // 수익현황( 월매출 / 기준 : 최근 12개월 )
    public List<IncomeResponseDto> getIncomeListMonthRecentYear(Long ownerId, LocalDate now) {
        return jpaQueryFactory.selectDistinct(new QIncomeResponseDto(
                dateFormatYYYYmm(null),
                order.totalAmount.sum()))
                .from(order)
                .join(vehicle)
                .on(order.vehicle.id.eq(vehicle.id))
                .where(vehicle.ownerId.eq(ownerId)
                        .and(order.status.ne(Status.CANCEL))
                        .and(dateFormatYYYY(null).eq(dateFormatYYYY(now))))
                .groupBy(dateFormatYYYYmm(null))
                .fetch();
    }

    private StringTemplate dateFormatYYYYmmdd() {

        return Expressions.stringTemplate(
                "DATE_FORMAT({0}, {1})",
                order.createdAt,
                ConstantImpl.create("%Y-%m-%d"));
    }

    private StringTemplate dateFormatYYYYmm(LocalDate now) {

        if (now == null) {
            return Expressions.stringTemplate(
                    "DATE_FORMAT({0}, {1})",
                    order.createdAt,
                    ConstantImpl.create("%Y-%m"));
        } else {
            return Expressions.stringTemplate(
                    "DATE_FORMAT({0}, {1})",
                    now,
                    ConstantImpl.create("%Y-%m"));

        }
    }

    private StringTemplate dateFormatYYYY(LocalDate now) {

        if (now == null) {
            return Expressions.stringTemplate(
                    "DATE_FORMAT({0}, {1})",
                    order.createdAt,
                    ConstantImpl.create("%Y"));
        } else {
            return Expressions.stringTemplate(
                    "DATE_FORMAT({0}, {1})",
                    now,
                    ConstantImpl.create("%Y"));

        }
    }

}



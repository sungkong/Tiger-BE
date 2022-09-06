package com.tiger.repository;

import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tiger.domain.order.QOrders;
import com.tiger.domain.order.Status;
import com.tiger.domain.order.dto.IncomeResponseDto;
import com.tiger.domain.order.dto.QIncomeResponseDto;
import com.tiger.domain.payment.QPayment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;
    QOrders order = QOrders.orders;
    QPayment payment = QPayment.payment;

    // 수익현황(일별매출 / 기준 : 당일 월 )
    public List<IncomeResponseDto> getIncomeListDay(Long memberId, LocalDate now) {
        return jpaQueryFactory.selectDistinct(new QIncomeResponseDto(
                dateFormatYYYYmmdd(),
                payment.paidAmount.sum()))
                .from(order)
                .join(payment)
                .on(order.id.eq(payment.order.id))
                .where(order.member.id.eq(memberId)
                        .and(dateFormatYYYYmm(null).eq(dateFormatYYYYmm(now)))
                        .and(order.status.ne(Status.CANCLE)))
                .groupBy(dateFormatYYYYmmdd())
                .fetch();
    }
    // 수익현황(일별매출 / 기준 : 당일 연도 )
    public List<IncomeResponseDto> getIncomeListMonth(Long memberId, LocalDate now) {
        return jpaQueryFactory.selectDistinct(new QIncomeResponseDto(
                dateFormatYYYYmm(null),
                payment.paidAmount.sum()))
                .from(order)
                .join(payment)
                .on(order.id.eq(payment.order.id))
                .where(order.member.id.eq(memberId)
                        .and(order.status.ne(Status.CANCLE))
                        .and(dateFormatYYYY(null).eq(dateFormatYYYY(now)))
                        .and(order.status.ne(Status.CANCLE)))
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



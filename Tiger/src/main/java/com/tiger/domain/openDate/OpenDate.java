package com.tiger.domain.openDate;

import com.tiger.domain.order.Orders;
import com.tiger.domain.payment.PayMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class OpenDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 임시
    @Column(nullable = false)
    private Long vehicleId; // 상품 식별번호

    @Column(nullable = false)
    private LocalDate start_date; // 시작 날짜

    @Column(nullable = false)
    private LocalDate end_date; // 시작 날짜
}

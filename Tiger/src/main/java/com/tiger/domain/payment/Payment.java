package com.tiger.domain.payment;

import com.tiger.domain.Timestamped;
import com.tiger.domain.order.Orders;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class Payment extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Orders order; // 주문

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PayMethod payMethod; // 결제수단

    @Column(nullable = false)
    private int paidAmount; // 결제 금액


}

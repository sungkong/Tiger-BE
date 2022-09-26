package com.tiger.domain.payout;

import com.tiger.domain.Timestamped;
import com.tiger.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class Payout extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member; //

    @Column(nullable = false)
    private LocalDateTime paymentDate; // 지급 날짜

    @Column(nullable = false)
    private int fee; // 수수료

    @Column(nullable = false)
    private int income; // 수익

    @Column(nullable = false)
    private int total_amount; // 최종 정산금
}

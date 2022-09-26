package com.tiger.domain.vehicle.heart;

import com.tiger.domain.Timestamped;
import com.tiger.domain.member.Member;
import com.tiger.domain.vehicle.Vehicle;
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
public class Heart extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "member_id")
    @ManyToOne
    private Member member;

    @JoinColumn(name = "vehicle_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Vehicle vehicle;

}

package com.tiger.domain.vehicle;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class VehicleImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String imageUrl;

    @JoinColumn(name = "vehicle_id") // 여기서 이렇게 연관관계를 매핑하는 게 맞는 건가?
    @ManyToOne(fetch = FetchType.LAZY)
    private Vehicle vehicle;
}

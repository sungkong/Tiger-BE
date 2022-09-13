package com.tiger.domain.openDate;

import com.tiger.domain.Timestamped;
import com.tiger.domain.vehicle.Vehicle;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.time.LocalDate;

import static javax.persistence.FetchType.*;

@Entity
@Getter
@NoArgsConstructor
public class OpenDate extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "START_DATE",nullable = false)
    private LocalDate startDate;

    @Column(name = "END_DATE",nullable = false)
    private LocalDate endDate;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "VEHICLE_ID")
    private Vehicle vehicle;

    @Builder
    public OpenDate(LocalDate startDate, LocalDate endDate , Vehicle vehicle){
        this.startDate = startDate;
        this.endDate = endDate;
        this.vehicle = vehicle;

    }

//    public void updateOpenDate(OpenDateRequestDto openDateRequestDto){
//        this.startDate = openDateRequestDto.getStartDate();
//        this.endDate =openDateRequestDto.getEndDate();
//
//    }



}

package com.tiger.domain.vehicle.review;
import com.tiger.domain.Timestamped;
import com.tiger.domain.member.Member;
import com.tiger.domain.vehicle.Vehicle;
import com.tiger.domain.vehicle.review.dto.ReviewRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Getter
@Entity
@NoArgsConstructor

public class Review extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "vehicle_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Vehicle vehicle;

    @JoinColumn(name = "member_id")
    @ManyToOne
    private Member member;

    @Column(nullable = false)
    private String comment;

    @Column(nullable = false)
    private Long rating;

    public Review update(ReviewRequestDto reviewRequestDto){
        this.comment =reviewRequestDto.getComment();
        this.rating = reviewRequestDto.getRating();

        return this;
    }

    @Builder
    public Review(Vehicle vehicle, String comment, Long rating,Member member) {
        this.vehicle = vehicle;
        this.comment = comment;
        this.rating = rating;
        this.member = member;
    }
}

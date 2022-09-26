package com.tiger.domain.vehicle.review.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewRequestDto {
    private Long rating;
    private String comment;
}

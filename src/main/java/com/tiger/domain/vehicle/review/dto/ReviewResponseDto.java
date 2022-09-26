package com.tiger.domain.vehicle.review.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReviewResponseDto {
    private Long rating;
    private String author;
    private String comment;
    private String createdAt;
}





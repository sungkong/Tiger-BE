package com.tiger.controller;

import com.tiger.domain.CommonResponseDto;
import com.tiger.domain.vehicle.review.dto.ReviewRequestDto;
import com.tiger.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


//       /api/vehicle/review/{vid} Post Get
//        /api/vehicle/review/{vid}/{commentId} PUT Delete
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/vehicle")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/review/{vid}") // Review 작성
    public CommonResponseDto<?> postReview(@RequestBody ReviewRequestDto reviewRequestDto, @PathVariable Long vid) {

        return reviewService.postReview(reviewRequestDto,vid);

    }

    @GetMapping("/review/{vid}") // ReviewList 조회
    public CommonResponseDto<?> getReviewList(@PathVariable Long vid) {
        return reviewService.getReviewList(vid);
    }


    @PutMapping("/review/{reviewId}") // Review 수정?
    public CommonResponseDto<?> updateReviewList(@RequestBody ReviewRequestDto reviewRequestDto,@PathVariable Long reviewId) {
        return reviewService.updateReview(reviewRequestDto,reviewId);
    }


    @DeleteMapping("/review/{reviewId}") // Review 삭제
    public CommonResponseDto<?> deleteReview(@PathVariable Long reviewId){

        return reviewService.deleteReview(reviewId);
    }


}




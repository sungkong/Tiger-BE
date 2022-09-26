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
    public CommonResponseDto<?> postAndUpdateReview(@RequestBody ReviewRequestDto reviewRequestDto, @PathVariable Long vid) {

        return reviewService.postAndUpdateReview(reviewRequestDto,vid);

    }

    @GetMapping("/review/{vid}") // ReviewList 조회
    public CommonResponseDto<?> getReviewList(@PathVariable Long vid) {
        return reviewService.getReviewList(vid);
    }

    @GetMapping("/reviewed/{vid}") // reviewed 조회
    public CommonResponseDto<?> getReviewed(@PathVariable Long vid) {
        return reviewService.getReviewed(vid);
    }


//    @PutMapping("/review/{vid}") // Review 수정?
//    public CommonResponseDto<?> updateReviewList(@RequestBody ReviewRequestDto reviewRequestDto,@PathVariable Long vid) {
//        return reviewService.updateReview(reviewRequestDto,vid);
//    }


    @DeleteMapping("/review/{vid}") // Review 삭제
    public CommonResponseDto<?> deleteReview(@PathVariable Long vid){

        return reviewService.deleteReview(vid);
    }


}




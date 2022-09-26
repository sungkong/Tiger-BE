package com.tiger.service;

import com.tiger.domain.CommonResponseDto;
import com.tiger.domain.member.Member;
import com.tiger.domain.vehicle.Vehicle;
import com.tiger.domain.vehicle.review.Review;
import com.tiger.domain.vehicle.review.dto.ReviewRequestDto;
import com.tiger.domain.vehicle.review.dto.ReviewResponseDto;
import com.tiger.exception.CustomException;
import com.tiger.repository.ReviewRepository;
import com.tiger.utils.CheckUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.tiger.exception.StatusCode.*;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final CheckUtil checkUtil;


    @Transactional
    public CommonResponseDto<?> postReview(ReviewRequestDto reviewRequestDto, Long vid) {
        Member member = checkUtil.validateMember();
        Vehicle findvehicle = checkUtil.validateVehicle(vid);

        if (isPresentReview(member.getEmail())) {
            return CommonResponseDto.fail(REVIEW_NO_MORE); // 1인 1리뷰 한정
        }

        reviewRepository.save(Review.builder()
                .vehicle(findvehicle)
                .comment(reviewRequestDto.getComment())
                .rating(reviewRequestDto.getRating())
                .build());

        return CommonResponseDto.success(REVIEW_SUCCESS, null);

    }

    public CommonResponseDto<?> getReviewList(Long vid) {

        List<Review> findReviewList = reviewRepository.findAllByVehicleId(vid).orElseThrow(() -> new CustomException(VEHICLE_NOT_FOUND));

        List<ReviewResponseDto> reviewResponseDtoList = findReviewList.stream()
                .map(dto -> ReviewResponseDto.builder()
                        .rating(dto.getRating())
                        .author(dto.getCreatedBy())
                        .comment(dto.getComment())
                        .build())
                .collect(Collectors.toList());


        return CommonResponseDto.success(REVIEW_LIST_SUCCESS, reviewResponseDtoList);
    }

    public CommonResponseDto<?> deleteReview(Long reviewId) {
        checkUtil.validateMember();
        reviewRepository.findById(reviewId).orElseThrow(() -> new CustomException(REVIEW_NOT_FOUND));

        reviewRepository.deleteById(reviewId);

        return CommonResponseDto.success(REVIEW_DELETED, null);
    }

    public CommonResponseDto<?> updateReview(ReviewRequestDto reviewRequestDto, Long reviewId) {
        checkUtil.validateMember();

        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new CustomException(REVIEW_NOT_FOUND));

        Review newReview = review.update(reviewRequestDto);

        reviewRepository.save(newReview);

        return CommonResponseDto.success(REVIEW_UPDATED,reviewId);
    }


    public boolean isPresentReview(String email) {
        return reviewRepository.existsByCreatedBy(email);
    }

}

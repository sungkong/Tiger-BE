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

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static com.tiger.exception.StatusCode.*;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final CheckUtil checkUtil;


    @Transactional
    public CommonResponseDto<?> postAndUpdateReview(ReviewRequestDto reviewRequestDto, Long vid) {
        Member findmember = checkUtil.validateMember();
        Vehicle findvehicle = checkUtil.validateVehicle(vid);

        if (isPresentReview(findmember,findvehicle.getId())) {

            return updateReview(reviewRequestDto,vid,findmember); // 1인 1차 1리뷰 한정
        }

        reviewRepository.save(Review.builder()
                .vehicle(findvehicle)
                .comment(reviewRequestDto.getComment())
                .rating(reviewRequestDto.getRating())
                .member(findmember)
                .build());

        return CommonResponseDto.success(REVIEW_SUCCESS, null);

    }
    @Transactional
    public CommonResponseDto<?> getReviewList(Long vid) {

        List<Review> findReviewList = reviewRepository.findAllByVehicleId(vid).orElseThrow(() -> new CustomException(VEHICLE_NOT_FOUND));

        List<ReviewResponseDto> reviewResponseDtoList = findReviewList.stream()
                .map(dto -> ReviewResponseDto.builder()
                        .rating(dto.getRating())
                        .author(dto.getMember().getName())
                        .comment(dto.getComment())
                        .createdAt(dto.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                        .build())
                .collect(Collectors.toList());

//    LocalDateTime d = LocalDateTime.parse("2016-10-31 23:59:59",
//            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return CommonResponseDto.success(REVIEW_LIST_SUCCESS, reviewResponseDtoList);
    }
    @Transactional
    public CommonResponseDto<?> getReviewed(Long vid) {
        Member member = checkUtil.validateMember();

        Review findReview = reviewRepository.findByMemberAndVehicleId(member,vid).orElseThrow(() -> new CustomException(REVIEW_NOT_FOUND));


       ReviewResponseDto reviewResponseDto = ReviewResponseDto.builder()
                .author(findReview.getMember().getName())
                .comment(findReview.getComment())
                .rating(findReview.getRating())
                .createdAt(findReview.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .build();



        return CommonResponseDto.success(REVIEW_LIST_SUCCESS, reviewResponseDto);
    }
    @Transactional
    public CommonResponseDto<?> deleteReview(Long vid) {
        Member member = checkUtil.validateMember();

        if(!(reviewRepository.existsByMemberAndVehicleId(member,vid))) {
            return CommonResponseDto.success(REVIEW_NOT_FOUND, null);
        }

        reviewRepository.deleteByMemberAndVehicleId(member,vid);

        return CommonResponseDto.success(REVIEW_DELETED, null);
    }

    protected  CommonResponseDto<?> updateReview(ReviewRequestDto reviewRequestDto,Long vid,Member member) {

        Review review = reviewRepository.findByMemberAndVehicleId(member,vid).orElseThrow(() -> new CustomException(REVIEW_NOT_FOUND));

        Review newReview = review.update(reviewRequestDto);

        reviewRepository.save(newReview);

        return CommonResponseDto.success(REVIEW_UPDATED, member.getName());
    }


    public boolean isPresentReview(Member member,Long vid) {
        return reviewRepository.existsByMemberAndVehicleId(member,vid);
    }
}

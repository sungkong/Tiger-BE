package com.tiger.service;

import com.tiger.domain.member.Member;
import com.tiger.domain.vehicle.Vehicle;
import com.tiger.domain.vehicle.VehicleImage;
import com.tiger.domain.vehicle.dto.*;
import com.tiger.domain.vehicle.review.Review;
import com.tiger.exception.CustomException;
import com.tiger.exception.StatusCode;
import com.tiger.repository.*;
import com.tiger.utils.CheckUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final AwsS3Service awsS3Service;
    private final VehicleImageRepository vehicleImageRepository;
    private final MemberRepository memberRepository;
    private final VehicleCustomRepository vehicleCustomRepository;
    private final HeartRepository heartRepository;

    private final ReviewRepository reviewRepository;
    private final CheckUtil checkUtil;

    @Value("${s3.default.vehicle.image}")
    private String DEFAULT_VEHICLE_IMAGE;


    @Transactional
    public Vehicle createVehicle(VehicleRequestDto requestDto, Long ownerId) {

        List<MultipartFile> multipartFiles = requestDto.getImageList();

        List<String> imageUrlList = awsS3Service.uploadFile(multipartFiles);

        Vehicle vehicle = Vehicle.builder()
                .ownerId(ownerId)
                .price(requestDto.getPrice())
                .description(requestDto.getDescription())
                .location(requestDto.getLocation())
                .locationX(requestDto.getLocationX())
                .locationY(requestDto.getLocationY())
                .isValid(true)
                .thumbnail(imageUrlList.get(0))
                .vbrand(requestDto.getVbrand())
                .vname(requestDto.getVname())
                .type(requestDto.getType())
                .years(requestDto.getYears())
                .fuelType(requestDto.getFuelType())
                .passengers(requestDto.getPassengers())
                .transmission(requestDto.getTransmission())
                .fuelEfficiency(requestDto.getFuelEfficiency())
                .build();

        saveVehicleImages(imageUrlList, vehicle);

        return vehicleRepository.save(vehicle);
    }


    public Page<VehicleCommonResponseDto> readAllVehiclesByType(String type, HttpServletRequest request, Pageable pageable) {

        Member member = null;
        if (null != request.getHeader("RefreshToken") && null != request.getHeader("Authorization")) {
            member = checkUtil.validateMember();
        }

        Page<Vehicle> vehicleList = vehicleRepository.findAllByIsValid(true, pageable);


        List<VehicleCommonResponseDto> vehicleCommonResponseDtos = new ArrayList<>();

        for (Vehicle vehicle : vehicleList) {
            VehicleCommonResponseDto vehicleCommonResponseDto = new VehicleCommonResponseDto(vehicle);
            if (member != null) {
                if (heartRepository.findByMemberAndVehicle(member, vehicle).isPresent()) {
                    vehicleCommonResponseDto.setHeart(true);
                }
            }

            Long finalRating = calculateRating(vehicle.getId());
            vehicleCommonResponseDto.setAverageRating(finalRating);

            vehicleCommonResponseDtos.add(vehicleCommonResponseDto);

        }

        return new PageImpl(vehicleCommonResponseDtos, vehicleList.getPageable(), vehicleList.getTotalElements());

    }


    public VehicleDetailResponseDto readOneVehicle(Long vId, LocalDate startDate, LocalDate endDate, HttpServletRequest request) {

        Vehicle vehicle = findVehicleByVehicleId(vId);
        Member renter = null;
        if (null != request.getHeader("RefreshToken") && null != request.getHeader("Authorization")) {
            renter = checkUtil.validateMember();
        }

        Member owner = findMemberByMemberId(vehicle.getOwnerId());

        VehicleDetailResponseDto vehicleDetailResponseDto = new VehicleDetailResponseDto(vehicle, owner, startDate, endDate);
        if (renter != null) {
            if (heartRepository.findByMemberAndVehicle(renter, vehicle).isPresent()) {
                vehicleDetailResponseDto.setHeart(true);
            }
        }

        Long finalRating = calculateRating(vehicle.getId());
        vehicleDetailResponseDto.setAverageRating(finalRating);


        return vehicleDetailResponseDto;
    }


    public VehicleUpdatePageResponseDto readOneVehicleForVehicleUpdatePage(Long vId, Long ownerId) {

        Vehicle vehicle = findVehicleByVehicleId(vId);

        if (!vehicle.getOwnerId().equals(ownerId)) {
            throw new CustomException(StatusCode.INVALID_AUTH_UPDATE);
        }

        Member member = findMemberByMemberId(ownerId);

        return new VehicleUpdatePageResponseDto(vehicle, member, null, null);
    }


    public List<VehicleOwnerResponseDto> readAllVehiclesByOwnerId(Long ownerId) {

        Member member = findMemberByMemberId(ownerId);

        List<Vehicle> vehicleList = vehicleRepository.findAllByOwnerIdAndIsValidOrderByCreatedAtDesc(ownerId, true).orElseThrow(() -> {
            throw new CustomException(StatusCode.VEHICLE_NOT_FOUND);
        });

        List<VehicleOwnerResponseDto> vehicleOwnerResponseDtos = new ArrayList<>();

        for (Vehicle vehicle : vehicleList) {

            vehicleOwnerResponseDtos.add(
                    VehicleOwnerResponseDto.builder()
                            .vid(vehicle.getId())
                            .thumbnail(vehicle.getThumbnail())
                            .vbrand(vehicle.getVbrand())
                            .oname(member.getName())
                            .vname(vehicle.getVname())
                            .price(vehicle.getPrice())
                            .location(vehicle.getLocation())
                            .createdAt(vehicle.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                            .build()
            );
        }
        return vehicleOwnerResponseDtos;
    }


    @Transactional
    public String updateVehicle(Long vId, VehicleUpdateRequestDto requestDto, Long ownerId) {

        Vehicle vehicle = findVehicleByVehicleId(vId);

        if (!vehicle.getOwnerId().equals(ownerId)) {
            throw new CustomException(StatusCode.INVALID_AUTH_UPDATE);
        }

        List<String> removeList = requestDto.getRemoveList();

        if (!removeList.isEmpty()) {
            for (String imageUrl : removeList) {
                String key = imageUrl.substring(imageUrl.lastIndexOf("/")+1);
                awsS3Service.deleteFile(key);
                vehicleImageRepository.deleteByImageUrl(imageUrl);
            }
        }

        List<MultipartFile> multipartFiles = requestDto.getImageList();

        if (!multipartFiles.isEmpty()) {
            List<String> imageUrlList = awsS3Service.uploadFile(multipartFiles);
            saveVehicleImages(imageUrlList, vehicle);
        }

        vehicle.update(requestDto, ownerId);

        return vehicle.getVname();
    }


    @Transactional
    public String deleteVehicle(Long vId, Long ownerId) {

        Vehicle vehicle = findVehicleByVehicleId(vId);

        if (!vehicle.getOwnerId().equals(ownerId)) {
            throw new CustomException(StatusCode.INVALID_AUTH_UPDATE);
        }

        deleteAllVehicleImages(vId);

        vehicle.delete(DEFAULT_VEHICLE_IMAGE);

        return vehicle.getVname();
    }


    public Page<VehicleSearchResponseDto> searchVehicles(VehicleSearch vehicleSearch, HttpServletRequest request, Pageable pageable) {

        Member member = null;
        if (null != request.getHeader("RefreshToken") && null != request.getHeader("Authorization")) {
            member = checkUtil.validateMember();
        }

        LocalDate startDate = vehicleSearch.getStartDate();
        LocalDate endDate = vehicleSearch.getEndDate();
//        String location = vehicleSearch.getLocation();
        Double locationX = vehicleSearch.getLocationX();
        Double locationY = vehicleSearch.getLocationY();
        String type = vehicleSearch.getType();

        Page<VehicleCustomResponseDto> vehicleCustomResponseDtos = vehicleCustomRepository.searchVehicle(startDate, endDate, locationX, locationY, type, pageable);

        List<VehicleSearchResponseDto> vehicleSearchResponseDtos = new ArrayList<>();

        for (VehicleCustomResponseDto vehicleCustomResponseDto : vehicleCustomResponseDtos) {
            VehicleSearchResponseDto vehicleSearchResponseDto = new VehicleSearchResponseDto(vehicleCustomResponseDto, startDate, endDate);
            if (member != null) {
                if (heartRepository.findByMemberAndVehicleId(member, vehicleSearchResponseDto.getVid()).isPresent()) {
                    vehicleSearchResponseDto.setHeart(true);
                }
            }

            Long finalRating = calculateRating(vehicleSearchResponseDto.getVid());
            vehicleSearchResponseDto.setAverageRating(finalRating);


            vehicleSearchResponseDtos.add(vehicleSearchResponseDto);

        }

        return new PageImpl(vehicleSearchResponseDtos, vehicleCustomResponseDtos.getPageable(), vehicleCustomResponseDtos.getTotalElements());
    }



    @Transactional
    public VehicleThumbnailResponseDto updateVehicleThumbnail(Long vId, VehicleThumbnailRequestDto requestDto, Member member) {

        Vehicle vehicle = findVehicleByVehicleId(vId);
        if (!vehicle.getOwnerId().equals(member.getId())) {
            throw new CustomException(StatusCode.INVALID_AUTH_UPDATE);
        }

        // 기존 썸네일 삭제
        String oldThumbnail = requestDto.getOldThumbnail().get(0);
        String key = oldThumbnail.substring(oldThumbnail.lastIndexOf("/")+1);
        awsS3Service.deleteFile(key);
        vehicleImageRepository.deleteByImageUrl(oldThumbnail);

        // 새로운 썸네일 업로드 및 저장
        List<MultipartFile> newThumbnailFile = requestDto.getNewThumbnail();
        List<String> newThumbnailUrlList = awsS3Service.uploadFile(newThumbnailFile);
        String newThumbnailUrl = newThumbnailUrlList.get(0);
        VehicleImage newVehicleThumbnailImage = VehicleImage.builder()
                .imageUrl(newThumbnailUrl)
                .vehicle(vehicle)
                .build();
        vehicleImageRepository.save(newVehicleThumbnailImage);

        // 새로운 썸네일 수정
        vehicle.updateThumbnail(newThumbnailUrl);
        String thumbnail = vehicle.getThumbnail();

        // VehicleThumbnailResponseDto에 담기
        return new VehicleThumbnailResponseDto(thumbnail);

    }




    public Vehicle findVehicleByVehicleId(Long vId) {
        return vehicleRepository.findByIdAndIsValid(vId, true).orElseThrow(() -> {
            throw new CustomException(StatusCode.VEHICLE_NOT_FOUND);
        });
    }

    public Member findMemberByMemberId(Long mId) {
        return memberRepository.findByIdAndIsValid(mId, true).orElseThrow(() -> {
            throw new CustomException(StatusCode.USER_NOT_FOUND);
        });
    }

    public void deleteAllVehicleImages(Long vId) {
        List<VehicleImage> vehicleImages = vehicleImageRepository.findAllByVehicle_Id(vId);
        for (VehicleImage vehicleImage : vehicleImages) {

            String imageUrl = vehicleImage.getImageUrl();
            String key = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
            awsS3Service.deleteFile(key);
        }
        vehicleImageRepository.deleteAllByVehicle_Id(vId);
    }

    public void saveVehicleImages(List<String> imageUrlList, Vehicle vehicle) {
        for (String imageUrl : imageUrlList) {
            vehicleImageRepository.save(
                    VehicleImage.builder()
                            .imageUrl(imageUrl)
                            .vehicle(vehicle)
                            .build()
            );
        }
    }

    public Long calculateRating(Long vid){
        List<Review> reviewList = reviewRepository.findAllByVehicleId(vid).orElse(null);

        if (reviewList != null){

            double sum = 0L;
            for (Review review: reviewList){
                Long rating = review.getRating();
                sum += rating;
            }
            return Math.round(sum / reviewList.size());
        }

        return null;
    }


}

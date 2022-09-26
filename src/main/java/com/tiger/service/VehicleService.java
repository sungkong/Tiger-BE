package com.tiger.service;

import com.tiger.domain.member.Member;
import com.tiger.domain.vehicle.Vehicle;
import com.tiger.domain.vehicle.VehicleImage;
import com.tiger.domain.vehicle.dto.*;
import com.tiger.exception.CustomException;
import com.tiger.exception.StatusCode;
import com.tiger.repository.*;
import com.tiger.utils.CheckUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
    private final CheckUtil checkUtil;

    @Value("${s3.default.vehicle.image}")
    private String DEFAULT_VEHICLE_IMAGE;


    // 상품 등록
    @Transactional
    public Vehicle create(VehicleRequestDto requestDto, Long ownerId) {

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

    // 종류별 상품 조회
    public List<VehicleCommonResponseDto> readAllByType(String type, HttpServletRequest request) {

        Member member = null;
        if (null != request.getHeader("RefreshToken") && null != request.getHeader("Authorization")) {
            member = checkUtil.validateMember();
        }

        List<Vehicle> vehicleList = vehicleRepository.findAllByTypeAndIsValidOrderByModifiedAtDesc(type, true).orElseThrow(()-> {
            throw new CustomException(StatusCode.VEHICLE_NOT_FOUND);
        });

        List<VehicleCommonResponseDto> vehicleCommonResponseDtos = new ArrayList<>();

        for (Vehicle vehicle : vehicleList) {
            VehicleCommonResponseDto vehicleCommonResponseDto = new VehicleCommonResponseDto(vehicle);
            if(member != null){
                if(heartRepository.findByMemberAndVehicle(member, vehicle).isPresent()){
                    vehicleCommonResponseDto.setHeart(true);
                }
            }
            vehicleCommonResponseDtos.add(vehicleCommonResponseDto);
//                VehicleCommonResponseDto.builder()
//                    .vid(vehicle.getId())
//                    .ownerId(vehicle.getOwnerId())
//                    .price(vehicle.getPrice())
//                    .description(vehicle.getDescription())
//                    .location(vehicle.getLocation())
//                    .locationX(vehicle.getLocationX())
//                    .locationY(vehicle.getLocationY())
//                    .imageList(vehicle.getImages().stream().map(VehicleImage::getImageUrl).collect(Collectors.toList()))
//                    .vbrand(vehicle.getVbrand())
//                    .vname(vehicle.getVname())
//                    .type(vehicle.getType())
//                    .years(vehicle.getYears())
//                    .fuelType(vehicle.getFuelType())
//                    .passengers(vehicle.getPassengers())
//                    .transmission(vehicle.getTransmission())
//                    .fuelEfficiency(vehicle.getFuelEfficiency())
//                    .build());

        }
        return vehicleCommonResponseDtos;
    }

    // 상세 상세 조회
    public VehicleDetailResponseDto readOne(Long vId, LocalDate startDate, LocalDate endDate, HttpServletRequest request) {

        Vehicle vehicle = findVehicleByVid(vId);
        Member renter = null;
        if (null != request.getHeader("RefreshToken") && null != request.getHeader("Authorization")) {
            renter = checkUtil.validateMember();
        }

        Member owner = findMemberByMid(vehicle.getOwnerId());

        VehicleDetailResponseDto vehicleDetailResponseDto = new VehicleDetailResponseDto(vehicle, owner, startDate, endDate);
        if(renter != null){
            if(heartRepository.findByMemberAndVehicle(renter, vehicle).isPresent()){
                vehicleDetailResponseDto.setHeart(true);
            }
        }
        return vehicleDetailResponseDto;
    }

    // 등록한 상품 수정
    public VehicleDetailResponseDto updatePage(Long vId, Long ownerId) {

        Vehicle vehicle = findVehicleByVid(vId);

        if (!vehicle.getOwnerId().equals(ownerId)) {
            throw new CustomException(StatusCode.INVALID_AUTH_UPDATE);
        }

        Member member = findMemberByMid(ownerId);

        return new VehicleDetailResponseDto(vehicle, member, null, null);
    }

    // 등록한 상품 조회
    public List<VehicleOwnerResponseDto> readAllByOwnerId(Long ownerId) {

        Member member = findMemberByMid(ownerId);

        List<Vehicle> vehicleList = vehicleRepository.findAllByOwnerIdAndIsValidOrderByCreatedAtDesc(ownerId, true).orElseThrow(()-> {
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

    // 상품 수정
    @Transactional
    public String update(Long vId, VehicleRequestDto requestDto, Long ownerId) {

        Vehicle vehicle = findVehicleByVid(vId);

        if (!vehicle.getOwnerId().equals(ownerId)) {
            throw new CustomException(StatusCode.INVALID_AUTH_UPDATE);
        }

        deleteAllImages(vId);

        List<MultipartFile> multipartFiles = requestDto.getImageList();

        List<String> imageUrlList = awsS3Service.uploadFile(multipartFiles);

        saveVehicleImages(imageUrlList, vehicle);

        vehicle.update(requestDto, ownerId, imageUrlList.get(0));

        return vehicle.getVname();
    }

    // 상품 삭제
    @Transactional
    public String delete(Long vId, Long ownerId) {

        Vehicle vehicle = findVehicleByVid(vId);

        if (!vehicle.getOwnerId().equals(ownerId)) {
            throw new CustomException(StatusCode.INVALID_AUTH_UPDATE);
        }

        deleteAllImages(vId);

        vehicle.delete(DEFAULT_VEHICLE_IMAGE);

        return vehicle.getVname();
    }

    // 상품 검색
    public List<VehicleSearchResponseDto> search(VehicleSearch vehicleSearch, HttpServletRequest request) {

        Member member = null;
        if (null != request.getHeader("RefreshToken") && null != request.getHeader("Authorization")) {
            member = checkUtil.validateMember();
        }

        LocalDate startDate =  vehicleSearch.getStartDate();
        LocalDate endDate = vehicleSearch.getEndDate();
//        String location = vehicleSearch.getLocation();
        Double locationX = vehicleSearch.getLocationX();
        Double locationY = vehicleSearch.getLocationY();
        String type = vehicleSearch.getType();

        List<VehicleCustomResponseDto> vehicleCustomResponseDtos = vehicleCustomRepository.searchVehicle(startDate, endDate, locationX, locationY, type);

        List<VehicleSearchResponseDto> vehicleSearchResponseDtos = new ArrayList<>();

        for (VehicleCustomResponseDto vehicleCustomResponseDto : vehicleCustomResponseDtos) {
            VehicleSearchResponseDto vehicleSearchResponseDto = new VehicleSearchResponseDto(vehicleCustomResponseDto, startDate, endDate);
            if(member != null){
                if(heartRepository.findByMemberAndVehicleId(member, vehicleSearchResponseDto.getVid()).isPresent()){
                    vehicleSearchResponseDto.setHeart(true);
                }
            }
            vehicleSearchResponseDtos.add(vehicleSearchResponseDto);
        }

        return vehicleSearchResponseDtos;
    }

    public Vehicle findVehicleByVid(Long vId) {
        return vehicleRepository.findByIdAndIsValid(vId, true).orElseThrow(() -> {
            throw new CustomException(StatusCode.VEHICLE_NOT_FOUND);
        });
    }

    public Member findMemberByMid(Long mId) {
        return memberRepository.findByIdAndIsValid(mId, true).orElseThrow(() -> {
            throw new CustomException(StatusCode.USER_NOT_FOUND);
        });
    }

    public void deleteAllImages(Long vId) {
        List<VehicleImage> vehicleImages = vehicleImageRepository.findAllByVehicle_Id(vId);
        for (VehicleImage vehicleImage : vehicleImages) {

            String imageUrl = vehicleImage.getImageUrl();
            String key = imageUrl.substring(imageUrl.lastIndexOf("/")+1);
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


}

package com.tiger.service;

import com.tiger.domain.member.Member;
import com.tiger.domain.vehicle.Vehicle;
import com.tiger.domain.vehicle.VehicleImage;
import com.tiger.domain.vehicle.dto.VehicleDetailResponseDto;
import com.tiger.domain.vehicle.dto.VehicleOwnerResponseDto;
import com.tiger.domain.vehicle.dto.VehicleRequestDto;
import com.tiger.domain.vehicle.dto.VehicleCommonResponseDto;
import com.tiger.exception.CustomException;
import com.tiger.exception.StatusCode;
import com.tiger.repository.MemberRepository;
import com.tiger.repository.VehicleImageRepository;
import com.tiger.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final AwsS3Service awsS3Service;
    private final VehicleImageRepository vehicleImageRepository;

    private final MemberRepository memberRepository;

    private static final String DEFAULT_VEHICLE_IMAGE = "https://mygitpher.s3.ap-northeast-2.amazonaws.com/DEFAULT_VEHICLE_IMAGE.png";

    // 상품 등록
    @Transactional
    public String create(VehicleRequestDto requestDto, Long ownerId) {

        List<MultipartFile> multipartFiles = requestDto.getImageList();

        List<String> imageUrlList = awsS3Service.uploadFile(multipartFiles);

        Vehicle vehicle = Vehicle.builder()
                .ownerId(ownerId)
                .price(requestDto.getPrice())
                .description(requestDto.getDescription())
                .location(requestDto.getLocation())
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

        for (String imageUrl : imageUrlList) {
            vehicleImageRepository.save(
                VehicleImage.builder()
                        .imageUrl(imageUrl)
                        .vehicle(vehicle)
                        .build()
            );
        }

        return vehicleRepository.save(vehicle).getVname();
    }

    // 종류별 상품 조회
    public List<VehicleCommonResponseDto> readAllByType(String type) {

        List<Vehicle> vehicleList = vehicleRepository.findAllByTypeAndIsValidOrderByModifiedAtDesc(type, true).orElseThrow(()-> {
            throw new CustomException(StatusCode.VEHICLE_NOT_FOUND);
        });

        List<VehicleCommonResponseDto> vehicleCommonResponseDtos = new ArrayList<>();

        for (Vehicle vehicle : vehicleList) {

            vehicleCommonResponseDtos.add(
                VehicleCommonResponseDto.builder()
                    .vid(vehicle.getId())
                    .ownerId(vehicle.getOwnerId())
                    .price(vehicle.getPrice())
                    .description(vehicle.getDescription())
                    .location(vehicle.getLocation())
                    .imageList(vehicle.getImages().stream().map(VehicleImage::getImageUrl).collect(Collectors.toList()))
                    .vbrand(vehicle.getVbrand())
                    .vname(vehicle.getVname())
                    .type(vehicle.getType())
                    .years(vehicle.getYears())
                    .fuelType(vehicle.getFuelType())
                    .passengers(vehicle.getPassengers())
                    .transmission(vehicle.getTransmission())
                    .fuelEfficiency(vehicle.getFuelEfficiency())
                    .build());

        }
        return vehicleCommonResponseDtos;
    }

    // 상세 상세 조회
    public VehicleDetailResponseDto readOne(Long vId) {

        Vehicle vehicle = vehicleRepository.findByIdAndIsValid(vId, true).orElseThrow(() -> {
            throw new CustomException(StatusCode.VEHICLE_NOT_FOUND);
        });

        Member member = memberRepository.findByIdAndIsValid(vehicle.getOwnerId(), true).orElseThrow(()->{
            throw new CustomException(StatusCode.USER_NOT_FOUND);
        });

        return new VehicleDetailResponseDto(vehicle, member);
    }

    // 등록한 상품 수정
    public VehicleDetailResponseDto updatePage(Long vId, Long ownerId) {

        Vehicle vehicle = vehicleRepository.findByIdAndIsValid(vId, true).orElseThrow(() -> {
            throw new CustomException(StatusCode.VEHICLE_NOT_FOUND);
        });

        if (!vehicle.getOwnerId().equals(ownerId)) {
            throw new CustomException(StatusCode.INVALID_AUTH_UPDATE);
        }

        Member member = memberRepository.findByIdAndIsValid(ownerId, true).orElseThrow(()->{
            throw new CustomException(StatusCode.USER_NOT_FOUND);
        });

        return new VehicleDetailResponseDto(vehicle, member);
    }

    // 등록한 상품 조회
    public List<VehicleOwnerResponseDto> readAllByOwnerId(Long ownerId) {

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

        Vehicle vehicle = vehicleRepository.findByIdAndIsValid(vId, true).orElseThrow(()->{
            throw new CustomException(StatusCode.VEHICLE_NOT_FOUND);
        });

        if (!vehicle.getOwnerId().equals(ownerId)) {
            throw new CustomException(StatusCode.INVALID_AUTH_UPDATE);
        }

        List<VehicleImage> vehicleImages = vehicleImageRepository.findAllByVehicle_Id(vId);
        for (VehicleImage vehicleImage : vehicleImages) {

            String imageUrl = vehicleImage.getImageUrl();
            String key = imageUrl.substring(imageUrl.lastIndexOf("/")+1);
            awsS3Service.deleteFile(key);
        }
        vehicleImageRepository.deleteAllByVehicle_Id(vId);

        List<MultipartFile> multipartFiles = requestDto.getImageList();

        List<String> imageUrlList = awsS3Service.uploadFile(multipartFiles);

        for (String imageUrl : imageUrlList) {
            vehicleImageRepository.save(
                    VehicleImage.builder()
                            .imageUrl(imageUrl)
                            .vehicle(vehicle)
                            .build()
            );
        }

        vehicle.update(requestDto, ownerId, imageUrlList.get(0));

        return vehicle.getVname();
    }

    // 상품 삭제
    @Transactional
    public String delete(Long vId, Long ownerId) {

        Vehicle vehicle = vehicleRepository.findByIdAndIsValid(vId, true).orElseThrow(()->{
            throw new CustomException(StatusCode.VEHICLE_NOT_FOUND);
        });

        if (!vehicle.getOwnerId().equals(ownerId)) {
            throw new CustomException(StatusCode.INVALID_AUTH_UPDATE);
        }

        List<VehicleImage> vehicleImages = vehicleImageRepository.findAllByVehicle_Id(vId);
        for (VehicleImage vehicleImage : vehicleImages) {

            String imageUrl = vehicleImage.getImageUrl();
            String key = imageUrl.substring(imageUrl.lastIndexOf("/")+1);
            awsS3Service.deleteFile(key);
        }
        vehicleImageRepository.deleteAllByVehicle_Id(vId);

        vehicle.delete(DEFAULT_VEHICLE_IMAGE);

        return vehicle.getVname();
    }
}

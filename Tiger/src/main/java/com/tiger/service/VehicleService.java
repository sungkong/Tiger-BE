package com.tiger.service;

import com.tiger.domain.vehicle.Vehicle;
import com.tiger.domain.vehicle.dto.VehicleRequestDto;
import com.tiger.domain.vehicle.dto.VehicleSearch;
import com.tiger.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    // 상품 등록
    @Transactional
    public Vehicle create(VehicleRequestDto requestDto) {

        Vehicle vehicle = Vehicle.builder()
                .ownerId(requestDto.getOwnerId())
                .price(requestDto.getPrice())
                .description(requestDto.getDescription())
                .address(requestDto.getAddress())
                .isValid(true)
                //
                .name(requestDto.getName())
                .type(requestDto.getType())
                .years(requestDto.getYears())
                .fuelType(requestDto.getFuelType())
                .transType(requestDto.getTransType())
                .fuelEfficiency(requestDto.getFuelEfficiency())
                .build();

        return vehicleRepository.save(vehicle);
    }

    // 종류별 상품 조회
    public List<Vehicle> readAllByType(String type) {

        return vehicleRepository.findAllByTypeAndIsValidOrderByModifiedAtDesc(type, true).orElseThrow(()-> new IllegalArgumentException("유효하지 않은 차종입니다."));

    }

    // 상세 상세 조회
    public Vehicle readOne(Long vId) {

        return vehicleRepository.findByIdAndIsValid(vId, true).
                orElseThrow(()-> new IllegalArgumentException("유효하지 않은 상품 식별번호입니다."));
    }

    // 등록한 상품 조회
    public List<Vehicle> readAllByOwnerId(Long ownerId) {

        return vehicleRepository.findAllByOwnerIdAndIsValidOrderByCreatedAtDesc(ownerId, true).orElseThrow(()-> new IllegalArgumentException("유효하지 않은 오너 식별번호입니다."));
    }

    // 상품 수정
    @Transactional
    public Vehicle update(Long vId, VehicleRequestDto requestDto) {

        Vehicle vehicle = VehicleService.this.readOne(vId);

        return vehicle.update(requestDto);
    }

    // 상품 삭제
    @Transactional
    public Vehicle delete(Long vId) {

        Vehicle vehicle = VehicleService.this.readOne(vId);

        return vehicle.delete();
    }

    /* 상품 검색
    public List<Vehicle> search(VehicleSearch vehicleSearch) {
        String location = vehicleSearch.getLocation();
        String startDate = vehicleSearch.getStartDate();
        String endDate = vehicleSearch.getEndDate();
        String type = vehicleSearch.getType();

        vehicleRepository.findAllByLocationAndStartDateAntEndDateAndType(location, startDate, endDate, type);

    }
    */



}

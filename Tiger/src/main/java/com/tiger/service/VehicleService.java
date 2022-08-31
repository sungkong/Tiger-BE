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

    @Transactional
    public Vehicle create(VehicleRequestDto requestDto) {

        Vehicle vehicle = Vehicle.builder()
                .owner(requestDto.getOwner())
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

    public List<Vehicle> readAllByType(String type) {

        return vehicleRepository.findAllByTypeOrderByModifiedAtDesc(type);

    }


//    public List<Vehicle> search(VehicleSearch vehicleSearch) {
//        String location = vehicleSearch.getLocation();
//        String startDate = vehicleSearch.getStartDate();
//        String endDate = vehicleSearch.getEndDate();
//        String type = vehicleSearch.getType();
//
//        vehicleRepository.findAllByLocationAndStartDateAntEndDateAndType(location, startDate, endDate, type);
//
//    }




}

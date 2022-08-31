package com.tiger.service;

import com.tiger.domain.vehicle.Vehicle;
import com.tiger.domain.vehicle.dto.VehicleRequestDto;
import com.tiger.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;


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




}

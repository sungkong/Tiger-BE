package com.tiger.controller;

import com.tiger.domain.vehicle.Vehicle;
import com.tiger.domain.vehicle.dto.VehicleRequestDto;
import com.tiger.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController @RequestMapping("/api/vehicle")
public class VehicleController {

    private final VehicleService vehicleService;


    @PostMapping("/management")
    public ResponseEntity<?> create(@RequestBody VehicleRequestDto requestDto) {

        Vehicle vehicle = vehicleService.create(requestDto);

        return ResponseEntity.ok(vehicle);
    }


}

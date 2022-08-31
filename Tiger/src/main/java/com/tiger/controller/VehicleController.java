package com.tiger.controller;

import com.tiger.domain.vehicle.Vehicle;
import com.tiger.domain.vehicle.dto.VehicleRequestDto;
import com.tiger.domain.vehicle.dto.VehicleSearch;
import com.tiger.service.VehicleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController @RequestMapping("/api/vehicle")
@Api(tags = "[상품 컨트롤러]")
public class VehicleController {

    private final VehicleService vehicleService;

    // 상품 등록
    @PostMapping("/management")
    @ApiOperation(value = "상품 등록", notes = "")
    public ResponseEntity<?> create(@RequestBody VehicleRequestDto requestDto) {

        Vehicle vehicle = vehicleService.create(requestDto);

        return ResponseEntity.ok(vehicle);
    }

    // 수입 상품 조회 (메인 페이지)
    @GetMapping
    public ResponseEntity<?> readAllVehiclesImported() {
        String type = "수입";

        List<Vehicle> vehicleList = vehicleService.readAllByType(type);

        return ResponseEntity.ok(vehicleList);
    }



    /* 상품 검색
    @GetMapping
    @ApiOperation(value = "상품 검색", notes = "")
    public ResponseEntity<?> search(@ModelAttribute VehicleSearch vehicleSearch)
    */






}

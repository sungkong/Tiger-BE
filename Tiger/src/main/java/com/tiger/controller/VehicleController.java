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
    @ApiOperation(value = "상품 등록")
    public ResponseEntity<?> create(@RequestBody VehicleRequestDto requestDto) {

        Vehicle vehicle = vehicleService.create(requestDto);

        return ResponseEntity.ok(vehicle);
    }

    // 수입 상품 조회 (메인페이지)
    @GetMapping
    @ApiOperation(value = "수입 상품 조회", notes = "메인페이지에서 사용")
    public ResponseEntity<?> readAllByTypeImported() {
        String type = "수입";

        List<Vehicle> vehicleList = vehicleService.readAllByType(type);

        return ResponseEntity.ok(vehicleList);
    }

    // 상품 상세 조회
    @GetMapping("/{vId}")
    @ApiOperation(value = "상품 상세 조회")
    public ResponseEntity<?> readOne(@PathVariable Long vId) {

        Vehicle vehicle = vehicleService.readOne(vId);

        return ResponseEntity.ok(vehicle);
    }


    //등록한 상품 조회 (오너 마이페이지)
    @GetMapping("/management/{ownerId}")
    @ApiOperation(value = "등록한 상품 조회", notes = "오너 모드 마이페이지에서 사용")
    public ResponseEntity<?> readAllByOwnerId(@PathVariable Long ownerId) {

        List<Vehicle> vehicleList = vehicleService.readAllByOwnerId(ownerId);

        return ResponseEntity.ok(vehicleList);
    }


    /* 상품 검색
    @GetMapping
    @ApiOperation(value = "상품 검색", notes = "")
    public ResponseEntity<?> search(@ModelAttribute VehicleSearch vehicleSearch)
    */






}

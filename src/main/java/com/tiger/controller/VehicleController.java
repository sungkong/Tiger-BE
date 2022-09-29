package com.tiger.controller;

import com.tiger.domain.CommonResponseDto;
import com.tiger.domain.UserDetailsImpl;
import com.tiger.domain.member.Member;
import com.tiger.domain.vehicle.Vehicle;
import com.tiger.domain.vehicle.dto.*;
import com.tiger.exception.StatusCode;
import com.tiger.service.OpenDateService;
import com.tiger.service.VehicleService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.naming.Binding;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/vehicle")
public class VehicleController {

    private final VehicleService vehicleService;

    private final OpenDateService openDateService;


    @ApiOperation(value = "차량 등록")
    @PostMapping("/management")
    public CommonResponseDto<?> createVehicle(@ModelAttribute @Valid VehicleRequestDto requestDto,
                                              @AuthenticationPrincipal UserDetails userDetails) {

        Member member = ((UserDetailsImpl) userDetails).getMember();

        Vehicle vehicle = vehicleService.createVehicle(requestDto, member.getId());

        openDateService.createEmptyList(vehicle); // 차량 생성시 빈 배열 만들기


        return CommonResponseDto.success(StatusCode.VEHICLE_CREATED, vehicle.getVname());
    }

    @ApiOperation(value = "수입 차량 조회")
    @GetMapping
    public CommonResponseDto<?> readAllVehiclesByTypeImported(HttpServletRequest request,
                                                              @PageableDefault(size = 20) Pageable pageable) {
        String type = "수입";

//        List<VehicleCommonResponseDto> vehicleCommonResponseDtos = vehicleService.readAllVehiclesByType(type, request, pageable);
        Page<VehicleCommonResponseDto> vehicleCommonResponseDtos = vehicleService.readAllVehiclesByType(type, request, pageable);

        return CommonResponseDto.success(StatusCode.SUCCESS, vehicleCommonResponseDtos);
    }

    @ApiOperation(value = "차량 상세 조회")
    @GetMapping("/{vId}")
    public CommonResponseDto<?> readOneVehicle(@PathVariable Long vId,
                                        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
                                        HttpServletRequest request) {

        VehicleDetailResponseDto vehicleDetailResponseDto = vehicleService.readOneVehicle(vId, startDate, endDate, request);

        return CommonResponseDto.success(StatusCode.SUCCESS, vehicleDetailResponseDto);
    }


    @ApiOperation(value = "내가 등록한 차량 조회")
    @GetMapping("/management")
    public CommonResponseDto<?> readAllVehiclesByOwnerId(@AuthenticationPrincipal UserDetails userDetails) {

        Member member = ((UserDetailsImpl) userDetails).getMember();

        List<VehicleOwnerResponseDto> vehicleOwnerResponseDtos = vehicleService.readAllVehiclesByOwnerId(member.getId());

        return CommonResponseDto.success(StatusCode.SUCCESS, vehicleOwnerResponseDtos);
    }

    @ApiOperation(value = "차량 수정 페이지 요청")
    @GetMapping("/management/{vId}")
    public CommonResponseDto<?> readOneVehicleForVehicleUpdatePage(@PathVariable Long vId,
                                           @AuthenticationPrincipal UserDetails userDetails) {

        Member member = ((UserDetailsImpl) userDetails).getMember();

        VehicleUpdatePageResponseDto vehicleUpdatePageResponseDto = vehicleService.readOneVehicleForVehicleUpdatePage(vId, member.getId());

        return CommonResponseDto.success(StatusCode.SUCCESS, vehicleUpdatePageResponseDto);
    }

    @ApiOperation(value = "차량 수정")
    @PutMapping("/management/{vId}")
    public CommonResponseDto<?> updateVehicle(@PathVariable Long vId,
                                              @ModelAttribute @Valid VehicleUpdateRequestDto requestDto,
                                              @AuthenticationPrincipal UserDetails userDetails) {

        Member member = ((UserDetailsImpl) userDetails).getMember();

        String name = vehicleService.updateVehicle(vId, requestDto, member.getId());

        return CommonResponseDto.success(StatusCode.VEHICLE_UPDATED, name);
    }

    @ApiOperation(value = "차량 삭제")
    @DeleteMapping("/management/{vId}")
    public CommonResponseDto<?> deleteVehicle(@PathVariable Long vId,
                                       @AuthenticationPrincipal UserDetails userDetails) {

        Member member = ((UserDetailsImpl) userDetails).getMember();

        String name = vehicleService.deleteVehicle(vId, member.getId());

        return CommonResponseDto.success(StatusCode.VEHICLE_DELETED, name);
    }

    @ApiOperation(value = "차량 검색")
    @PostMapping("/search")
    public CommonResponseDto<?> searchVehicles(@RequestBody @Valid VehicleSearch vehicleSearch,
                                               HttpServletRequest request,
                                               @PageableDefault(size = 6) Pageable pageable) {

        Page<VehicleSearchResponseDto> vehicleSearchResponseDtos = vehicleService.searchVehicles(vehicleSearch, request, pageable);

        return CommonResponseDto.success(StatusCode.SUCCESS, vehicleSearchResponseDtos);
    }

    @ApiOperation(value = "썸네일 이미지 수정")
    @PutMapping("/thumbnail/{vId}")
    public CommonResponseDto<?> updateThumbnail(@PathVariable Long vId,
                                                @ModelAttribute VehicleThumbnailRequestDto requestDto,
                                                @AuthenticationPrincipal UserDetails userDetails) {

        Member member = ((UserDetailsImpl) userDetails).getMember();

        VehicleThumbnailResponseDto vehicleThumbnailResponseDto = vehicleService.updateVehicleThumbnail(vId, requestDto, member);

        return CommonResponseDto.success(StatusCode.THUMBNAIL_UPDATED, vehicleThumbnailResponseDto);

    }




}

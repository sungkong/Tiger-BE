package com.tiger.controller;

import com.tiger.domain.CommonResponseDto;
import com.tiger.domain.UserDetailsImpl;
import com.tiger.domain.member.Member;
import com.tiger.domain.vehicle.dto.VehicleCommonResponseDto;
import com.tiger.domain.vehicle.dto.VehicleDetailResponseDto;
import com.tiger.domain.vehicle.dto.VehicleOwnerResponseDto;
import com.tiger.domain.vehicle.dto.VehicleRequestDto;
import com.tiger.exception.StatusCode;
import com.tiger.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController @RequestMapping("/api/vehicle")
public class VehicleController {

    private final VehicleService vehicleService;

    // 상품 등록
    @PostMapping("/management")
    public CommonResponseDto<?> create(@ModelAttribute VehicleRequestDto requestDto,
                                       @AuthenticationPrincipal UserDetails userDetails) {

        Member member = ((UserDetailsImpl) userDetails).getMember();

        String name = vehicleService.create(requestDto, member.getId());

        return CommonResponseDto.success(StatusCode.VEHICLE_CREATED, name);
    }

    // 수입 상품 조회 (메인페이지)
    @GetMapping
    public CommonResponseDto<?> readAllByTypeImported() {
        String type = "수입";

        List<VehicleCommonResponseDto> vehicleCommonResponseDtos = vehicleService.readAllByType(type);

        return CommonResponseDto.success(StatusCode.SUCCESS, vehicleCommonResponseDtos);
    }

    // 상품 상세 조회
    @GetMapping("/{vId}")
    public CommonResponseDto<?> readOne(@PathVariable Long vId) {

        VehicleDetailResponseDto vehicleDetailResponseDto = vehicleService.readOne(vId);

        return CommonResponseDto.success(StatusCode.SUCCESS, vehicleDetailResponseDto);
    }


    //등록한 상품 조회 (오너 마이페이지)
    @GetMapping("/management")
    public CommonResponseDto<?> readAllByOwnerId(@AuthenticationPrincipal UserDetails userDetails) {

        Member member = ((UserDetailsImpl) userDetails).getMember();

        List<VehicleOwnerResponseDto> vehicleOwnerResponseDtos = vehicleService.readAllByOwnerId(member.getId());

        return CommonResponseDto.success(StatusCode.SUCCESS, vehicleOwnerResponseDtos);
    }

    // 상품 수정페이지 요청
    @GetMapping("/management/{vId}")
    public CommonResponseDto<?> updatePage(@PathVariable Long vId,
                                           @AuthenticationPrincipal UserDetails userDetails) {

        Member member = ((UserDetailsImpl) userDetails).getMember();

        VehicleDetailResponseDto vehicleDetailResponseDto = vehicleService.updatePage(vId, member.getId());

        return CommonResponseDto.success(StatusCode.SUCCESS, vehicleDetailResponseDto);
    }

    // 상품 수정
    @PutMapping("/management/{vId}")
    public CommonResponseDto<?> update(@PathVariable Long vId,
                                       @ModelAttribute VehicleRequestDto requestDto,
                                       @AuthenticationPrincipal UserDetails userDetails) {

        Member member = ((UserDetailsImpl) userDetails).getMember();

        String name = vehicleService.update(vId, requestDto, member.getId());

        return CommonResponseDto.success(StatusCode.VEHICLE_UPDATED, name);
    }

    // 상품 삭제
    @DeleteMapping("/management/{vId}")
    public CommonResponseDto<?> delete(@PathVariable Long vId,
                                       @AuthenticationPrincipal UserDetails userDetails) {

        Member member = ((UserDetailsImpl) userDetails).getMember();

        String name = vehicleService.delete(vId, member.getId());

        return CommonResponseDto.success(StatusCode.VEHICLE_DELETED, name);
    }
}

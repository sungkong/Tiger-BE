package com.tiger.service;

import com.tiger.domain.CommonResponseDto;
import com.tiger.domain.member.Member;
import com.tiger.domain.vehicle.Vehicle;
import com.tiger.domain.vehicle.dto.VehicleOwnerResponseDto;
import com.tiger.domain.vehicle.heart.Heart;
import com.tiger.exception.StatusCode;
import com.tiger.repository.HeartRepository;
import com.tiger.repository.VehicleRepository;
import com.tiger.utils.CheckUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.tiger.exception.StatusCode.*;

@Service
@RequiredArgsConstructor
public class HeartService {

    private final HeartRepository heartRepository;
    private final CheckUtil checkUtil;

    @Transactional
    public CommonResponseDto<?> heartVehicle(Long vehicleId) {

        Member member = checkUtil.validateMember();

        Vehicle vehicle = checkUtil.validateVehicle(vehicleId);

        Heart heart = isPresentHeart(member, vehicle);
        if (null == heart) {
            heartRepository.save(
                    heart.builder()
                            .member(member)
                            .vehicle(vehicle)
                            .build()
            );
            return CommonResponseDto.success(HEART_SUCCESS, null);
        } else {
            heartRepository.delete(heart);
            return CommonResponseDto.success(HEART_DELETED, null);
        }
    }

    @Transactional(readOnly = true)
    public CommonResponseDto<?> getAllHeartList() {
        Member member = checkUtil.validateMember();

        List<Heart> heartList = heartRepository.findAllByMember(member);
        List<VehicleOwnerResponseDto> vehicleResponseDtoList = new ArrayList<>();
        for (Heart heart : heartList) {
            Vehicle vehicle = heart.getVehicle();
            vehicleResponseDtoList.add(
                        VehicleOwnerResponseDto.builder()
                                .vid(vehicle.getId())
                                .thumbnail(vehicle.getThumbnail())
                                .vbrand(vehicle.getVbrand())
                                .vname(vehicle.getVname())
                                .price(vehicle.getPrice())
                                .location(vehicle.getLocation())
                                .build()
                );
            }
        return CommonResponseDto.success(HEARTLIST_SUCCESS, vehicleResponseDtoList);
    }

    @Transactional(readOnly = true)
    public Heart isPresentHeart(Member member, Vehicle vehicle) {
        return heartRepository.findByMemberAndVehicle(member, vehicle).orElse(null);
    }

    // 좋아요 개수
    @Transactional(readOnly = true)
    public int countHeart(Vehicle vehicle) {
        List<Heart> heartList = heartRepository.findAllByVehicle(vehicle);
        return heartList.size();
    }

}

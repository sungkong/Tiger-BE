package com.tiger.controller;

import com.tiger.domain.CommonResponseDto;
import com.tiger.domain.UserDetailsImpl;
import com.tiger.domain.member.Member;
import com.tiger.domain.openDate.dto.OpenDateListRequestDto;
import com.tiger.service.OpenDateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static com.tiger.exception.StatusCode.*;


@RequestMapping("/api/vehicle")
@RequiredArgsConstructor
@RestController
@Api(tags = "[OpenDateController]")
public class OpenDateController {
    private final OpenDateService openDateService;

    @PostMapping("/schedule/{vid}")
    @ApiOperation(value = "차량 오픈스케줄 등록")
    public CommonResponseDto<?> createOpenDate(@PathVariable Long vid,
                                               @RequestBody
                                               @DateTimeFormat(pattern = "yyyy-MM-dd")
                                               OpenDateListRequestDto openDateListRequestDto
                                               ) {


        openDateService.createOpenDate(openDateListRequestDto,vid);

        return CommonResponseDto.success(SCHEDULE_SUCCESS, null);
    }

    @GetMapping("/schedule/{vid}")
    @ApiOperation(value = "차량 오픈스케줄 수정시, 이미 예약된 날짜 불러오기")
    public CommonResponseDto<?> getOpenAndReservedDate(@PathVariable Long vid) {

        List<LocalDate> getOpenDate = openDateService.getOpenAndReservedDate(vid);

        @Getter
        @AllArgsConstructor
        class output {
            private Long vid;
            private List<LocalDate> OpenDateList;
        }


        return CommonResponseDto.success(GET_SCHEDULE_SUCCESS, new output(vid, getOpenDate));
    }


}

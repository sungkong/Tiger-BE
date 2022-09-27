package com.tiger.controller;

import com.tiger.domain.CommonResponseDto;
import com.tiger.service.HeartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/heart")
public class HeartController {

    private final HeartService heartService;

    @RequestMapping(value = "/vehicle/{id}", method = RequestMethod.POST)
    public CommonResponseDto<?> heartVehicle(
            @PathVariable Long id
    ) {
        return heartService.heartVehicle(id);
    }

    @RequestMapping(value = "/vehicle", method = RequestMethod.GET)
    public CommonResponseDto<?> getAllHeartList() {
        return heartService.getAllHeartList();
    }
}

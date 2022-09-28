package com.tiger.domain.vehicle.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class VehicleThumbnailResponseDto {

    private List<String> newThumbnail;


    public VehicleThumbnailResponseDto(String thumbnailUrl) {

        List<String> thumbnailList = new ArrayList<>();
        thumbnailList.add(thumbnailUrl);

        this.newThumbnail = thumbnailList;

    }


}

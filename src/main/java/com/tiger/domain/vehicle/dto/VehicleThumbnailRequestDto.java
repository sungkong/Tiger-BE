package com.tiger.domain.vehicle.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class VehicleThumbnailRequestDto {

    private List<String> oldThumbnail;

    private List<MultipartFile> newThumbnail;
}

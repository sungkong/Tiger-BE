package com.tiger.domain.openDate.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class OpenDateListRequestDto {

    private List<LocalDate> openDateList;

}

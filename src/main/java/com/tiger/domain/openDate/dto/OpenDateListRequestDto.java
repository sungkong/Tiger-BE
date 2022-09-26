package com.tiger.domain.openDate.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class OpenDateListRequestDto {

    private List<LocalDate> openDateList;

}

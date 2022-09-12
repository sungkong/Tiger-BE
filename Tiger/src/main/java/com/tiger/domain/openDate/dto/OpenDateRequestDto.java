package com.tiger.domain.openDate.dto;

import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@ToString

public class OpenDateRequestDto {
    private LocalDate startDate;
    private LocalDate endDate;


    @Builder
    public OpenDateRequestDto(LocalDate startDate) {
        this.startDate = startDate;

    }
}

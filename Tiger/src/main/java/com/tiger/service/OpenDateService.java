package com.tiger.service;


import com.tiger.domain.openDate.OpenDate;
import com.tiger.domain.openDate.dto.OpenDateListRequestDto;
import com.tiger.domain.openDate.dto.OpenDateRequestDto;
import com.tiger.domain.vehicle.Vehicle;
import com.tiger.exception.CustomException;
import com.tiger.repository.OpenDateRepository;
import com.tiger.repository.VehicleRepository;
import com.tiger.utils.CheckUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.tiger.exception.StatusCode.*;

@Service
@RequiredArgsConstructor
public class OpenDateService {

    private final OpenDateRepository openDateRepository;
    private final VehicleRepository vehicleRepository;

    private final CheckUtil checkUtil;

    @Transactional
    public void createOpenDate(OpenDateListRequestDto openDateListRequestDto, Long vid) {

        checkUtil.validateMember();

        Vehicle findVehicle = vehicleRepository.findById(vid).orElseThrow(() -> new CustomException(VEHICLE_NOT_FOUND));

        List<LocalDate> openDateList = new ArrayList<>(openDateListRequestDto.getOpenDateList());

        Collections.sort(openDateList); // 날짜 정렬(빠른 순으로)

        // 연속된 날짜 판별
        // startDate, endDate 뽑아서 List 생성
        List<OpenDateRequestDto> newOpendateList = new ArrayList<>();

        if (openDateList.get(0).isBefore(LocalDate.now())) {

            throw new CustomException(INVALID_DATE);

        }

        OpenDateRequestDto newOpendate = new OpenDateRequestDto(openDateList.get(0)); //startDate 세팅

        for (int i = 0; i < openDateList.size(); i++) {

            LocalDate getopenDate = openDateList.get(i);

            if (openDateList.size() > i + 1) {


                LocalDate nextDate = getopenDate.plusDays(1);

                if (nextDate.isEqual(openDateList.get(i + 1))) {
                    newOpendate.setEndDate(nextDate);
                } else {
                    if (newOpendate.getEndDate() == null) { // 중간에 연속되는 수가 아닐때
                        newOpendate.setEndDate(getopenDate);
                    }

                    newOpendateList.add(newOpendate);
                    newOpendate = new OpenDateRequestDto(openDateList.get(i + 1));
                }
            }

        }
        if (newOpendate.getEndDate() == null) { //1개만 요청하거나 마지막 날짜 처리
            newOpendate.setEndDate(newOpendate.getStartDate());
        }

        newOpendateList.add(newOpendate); // from to 세팅해서 list에 넣기

        List<OpenDate> classifiedOpenDateList = newOpendateList.stream()
                //stream으로
                .map(dto -> OpenDate.builder()
                        .startDate(dto.getStartDate())
                        .endDate(dto.getEndDate())
                        .vehicle(findVehicle)
                        .build())
                .collect(Collectors.toList()); // OpenDate형으로 변환

        boolean find = openDateRepository.existsByVehicleId(vid); // 해당 vid에 대한 데이터 유무만 검증

        if (!find) { // 새로 생성할때

            openDateRepository.saveAll(classifiedOpenDateList);

        } else {
            List<OpenDate> findOpenDateList = openDateRepository.findAllByVehicleIdOrderByStartDateAsc(vid).orElseThrow(() -> new CustomException(VEHICLE_NOT_FOUND));

            openDateRepository.deleteAll(findOpenDateList); //기존 것 삭제
            openDateRepository.saveAll(classifiedOpenDateList); // 새로 insert
        }
    }

    @Transactional
    public List<String> getOpenDate(Long vid) {
        checkUtil.validateMember();
        List<OpenDate> findOpenDateList = openDateRepository.findAllByVehicleIdOrderByStartDateAsc(vid).orElseThrow(() -> new CustomException(VEHICLE_NOT_FOUND));
        List<LocalDate> dtoList = new ArrayList<>();
        for (OpenDate findOpenDate : findOpenDateList) {

            LocalDate startDate = findOpenDate.getStartDate();
            LocalDate endDate = findOpenDate.getEndDate();


            if (startDate.isEqual(endDate)) {
                dtoList.add(startDate);
            } else {
                dtoList.addAll(startDate.datesUntil(endDate.plusDays(1))
                        .collect(Collectors.toList()));

            }
        }

        List<String> stringList = new ArrayList<>();
        for (LocalDate revDate : dtoList) {
            String reservedDate = revDate.format(DateTimeFormatter.ofPattern("yyyy-M-d"));

            if(revDate.isBefore(LocalDate.now()))
                continue;
            stringList.add(reservedDate);
        }
        return stringList;


    }

    @Transactional
    public void createEmptyList(Vehicle vehicle){

        List<LocalDate> localList = new ArrayList<>();
        localList.add(LocalDate.now());

        List<OpenDate> emptyList = localList.stream()
                .map(dto -> OpenDate.builder()
                        .startDate(dto)
                        .endDate(dto)
                        .vehicle(vehicle)
                        .build())
                .collect(Collectors.toList()); // OpenDate형으로 변환

        System.out.println("emptyList = " + emptyList);

        openDateRepository.saveAll(emptyList);
    }

}



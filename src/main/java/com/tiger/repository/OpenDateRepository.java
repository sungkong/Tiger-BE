package com.tiger.repository;

import com.tiger.domain.openDate.OpenDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OpenDateRepository extends JpaRepository<OpenDate, Long> {

    Optional<List<OpenDate>> findAllByVehicleIdOrderByStartDate(long vehicleId);

    // 주문기간만 포함된 달의 openDate만 가져오기
    @Query(value = "SELECT * " +
            "FROM open_date " +
            "WHERE vehicle_id = :vehicleId " +
            "AND (DATE_FORMAT(:startDate, '%Y-%m') = DATE_FORMAT(start_date, '%Y-%m') " +
            "  OR DATE_FORMAT(:endDate, '%Y-%m') = DATE_FORMAT(end_date, '%Y-%m'))", nativeQuery = true)
    Optional<List<OpenDate>> findAllByIncludeOrderDateMonth(@Param("vehicleId") Long vehicleId,
                                                            @Param("startDate") LocalDate startDate,
                                                            @Param("endDate") LocalDate endDate);

    Optional<List<OpenDate>> findAllByVehicleIdOrderByStartDateAsc(Long vid);

    boolean existsByVehicleId(Long vid);


    @Query(value = "SELECT *" +
            "FROM open_date" +
            "WHERE (DATE_FORMAT(now(), '%Y-%m-%d') >  DATE_FORMAT(end_date, '%Y-%m-%d'))"
           , nativeQuery = true)
    Optional<List<OpenDate>> findAllByEndDatePassed();
}

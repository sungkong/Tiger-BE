package com.tiger.repository;

import com.tiger.domain.openDate.OpenDate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OpenDateRepository extends JpaRepository<OpenDate, Long> {

    Optional<List<OpenDate>> findAllByVehicleId(long vehicleId);
}

package com.tiger.repository;

import com.tiger.domain.vehicle.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {


    // 과연 수정시간의 내림차순으로 정렬하는 게 서비스 관점에서 옳은가?
    Optional<List<Vehicle>> findAllByTypeAndIsValidOrderByModifiedAtDesc(String type, Boolean isValid);

    Optional<List<Vehicle>> findAllByOwnerIdAndIsValidOrderByCreatedAtDesc(Long ownerId, Boolean isValid);

    Optional<Vehicle> findByIdAndIsValid(Long id, Boolean isValid);


}

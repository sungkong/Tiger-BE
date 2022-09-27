package com.tiger.repository;

import com.tiger.domain.vehicle.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {


    Optional<List<Vehicle>> findAllByIsValid(boolean isValid);

    Optional<List<Vehicle>> findAllByOwnerIdAndIsValidOrderByCreatedAtDesc(Long ownerId, boolean isValid);

    Optional<Vehicle> findByIdAndIsValid(Long id, boolean isValid);

}

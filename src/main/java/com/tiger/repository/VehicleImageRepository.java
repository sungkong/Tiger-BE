package com.tiger.repository;

import com.tiger.domain.vehicle.VehicleImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VehicleImageRepository extends JpaRepository<VehicleImage, Long> {

    List<VehicleImage> findAllByVehicle_Id(Long id);

    void deleteAllByVehicle_Id(Long id);

    void deleteByImageUrl(String imageUrl);
}

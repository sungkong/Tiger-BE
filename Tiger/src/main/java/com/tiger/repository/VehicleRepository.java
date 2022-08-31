package com.tiger.repository;

import com.tiger.domain.vehicle.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {


    // 과연 수정시간의 내림차순으로 정렬하는 게 서비스 관점에서 옳은가?
    List<Vehicle> findAllByTypeOrderByModifiedAtDesc(String type);
}

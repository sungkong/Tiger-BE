package com.tiger.repository;

import com.tiger.domain.member.Member;
import com.tiger.domain.vehicle.Vehicle;
import com.tiger.domain.vehicle.heart.Heart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HeartRepository extends JpaRepository<Heart, Long> {

    Optional<Heart> findByMemberAndVehicle(Member member, Vehicle vehicle);
    Optional<Heart> findByMemberAndVehicleId(Member member, Long vehicleId);
    List<Heart> findAllByVehicle(Vehicle vehicle);
    List<Heart> findAllByMember(Member member);
    Optional<List<Heart>> findAllByMemberAndVehicleIsValid(Member member, Boolean vehicle_isValid);


}


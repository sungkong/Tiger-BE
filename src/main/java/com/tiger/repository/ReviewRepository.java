package com.tiger.repository;

import com.tiger.domain.vehicle.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    boolean existsByCreatedBy(String email);

    Optional<List<Review>> findAllByVehicleId(Long vid);

}

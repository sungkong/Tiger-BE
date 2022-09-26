package com.tiger.repository;

import com.tiger.domain.member.Member;
import com.tiger.domain.vehicle.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    boolean existsByMember(Member member);

    void deleteByMemberAndVehicleId(Member member,Long vid);

    Optional<Review> findByMemberAndVehicleId(Member member, Long vid);

    Optional<List<Review>> findAllByVehicleId(Long vid);

}

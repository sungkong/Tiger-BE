package com.tiger.repository;

import com.tiger.domain.member.Member;
import com.tiger.domain.order.Orders;
import com.tiger.domain.order.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Orders, Long> {

    Optional<List<Orders>> findAllByMemberId(Long memberId);

    Optional<List<Orders>> findAllByVehicleId(long vehicleId);

    // 판매리스트 (오너)
    @Query(value =  "SELECT * " +
                    "FROM orders " +
                    "WHERE member_id = :memberId " +
                    "AND status = :status " +
                    "ORDER BY created_at desc " +
                    "limit :limit offset :offset ", nativeQuery = true)
    Optional<List<Orders>> getOrderList(@Param("memberId") Long memberId,
                                             @Param("status") String status,
                                             @Param("limit") int limit,
                                             @Param("offset") int offset);
}

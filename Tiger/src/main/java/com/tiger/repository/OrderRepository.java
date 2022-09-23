package com.tiger.repository;

import com.tiger.domain.member.Member;
import com.tiger.domain.order.Orders;
import com.tiger.domain.order.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Orders, Long> {

    Optional<List<Orders>> findAllByMemberId(Long memberId);

    Optional<List<Orders>> findAllByVehicleId(Long vehicleId);

    Optional<List<Orders>> findAllByVehicleIdAndStatusNotOrderByStartDateAsc(Long vid,Status status);

    Optional<List<Orders>> findAllByVehicleIdAndStatusNot(Long vehicleId, Status status);

    // 최적화 해야함
    // 주문리스트 (렌터)
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

    // 현재 날짜와 예약 시작날짜가 같은 상품들 조회하기
    Optional<List<Orders>> findAllByStartDateEquals(LocalDate now);

}

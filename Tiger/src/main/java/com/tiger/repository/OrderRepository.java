package com.tiger.repository;

import com.tiger.domain.order.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Orders, Long> {
    Optional<List<Orders>> findAllByMemberId(Long memberId);
}

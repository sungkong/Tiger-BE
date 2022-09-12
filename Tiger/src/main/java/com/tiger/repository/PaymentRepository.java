package com.tiger.repository;

import com.tiger.domain.payment.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<List<Payment>> findAllByOrderId(long OrderId);
}

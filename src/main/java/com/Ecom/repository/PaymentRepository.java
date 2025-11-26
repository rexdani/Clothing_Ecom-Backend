package com.Ecom.repository;

import com.Ecom.model.Payment;
import com.Ecom.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    // Find payment by order
    Optional<Payment> findByOrder(Order order);
}

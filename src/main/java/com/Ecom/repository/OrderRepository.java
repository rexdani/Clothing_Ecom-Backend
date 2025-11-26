package com.Ecom.repository;

import com.Ecom.model.Order;
import com.Ecom.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // Find all orders of a specific user
    List<Order> findByUser(User user);
}

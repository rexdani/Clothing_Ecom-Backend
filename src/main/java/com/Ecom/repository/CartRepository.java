package com.Ecom.repository;

import com.Ecom.model.Cart;
import com.Ecom.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    // Find cart by user
    Optional<Cart> findByUser(User user);
    int countByUserId(Long userId);
}


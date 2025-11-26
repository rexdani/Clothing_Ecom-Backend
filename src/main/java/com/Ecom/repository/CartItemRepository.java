package com.Ecom.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.Ecom.model.CartItem;
import com.Ecom.model.Cart;
import com.Ecom.model.Product;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    // Get all items inside a cart
    List<CartItem> findByCart(Cart cart);

    // Find specific product inside the cart
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);

    // Count total number of cart items
    int countByCart(Cart cart);
}
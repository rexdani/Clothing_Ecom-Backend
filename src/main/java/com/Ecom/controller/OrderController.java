package com.Ecom.controller;

import com.Ecom.dto.PlaceOrderRequest;
import com.Ecom.model.Order;
import com.Ecom.service.OrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // ===================== PLACE ORDER =======================
    @PostMapping("/place")
    public ResponseEntity<Order> placeOrder(
            Authentication authentication,
            @RequestBody PlaceOrderRequest request
    ) {
        String email = authentication.getName();
        Order order = orderService.placeOrder(request, email);
        return ResponseEntity.ok(order);
    }

    // ===================== GET ALL USER ORDERS ===================
    @GetMapping
    public ResponseEntity<List<Order>> getUserOrders(Authentication authentication) {
        String email = authentication.getName();
        List<Order> orders = orderService.getOrdersByUser(email);
        return ResponseEntity.ok(orders);
    }

    // ===================== GET ORDER BY ID =======================
    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(
            Authentication authentication,
            @PathVariable Long orderId
    ) {
        String email = authentication.getName();
        Order order = orderService.getOrderById(orderId, email);
        return ResponseEntity.ok(order);
    }
}

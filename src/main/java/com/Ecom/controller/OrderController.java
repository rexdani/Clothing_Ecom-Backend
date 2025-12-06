package com.Ecom.controller;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.Authentication; // ✔ Correct import
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Ecom.dto.PlaceOrderRequest;
import com.Ecom.model.Order;
import com.Ecom.service.OrderService;

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

    // ===================== ADMIN: GET ALL ORDERS ===================
    @GetMapping("/admin/all")
    public ResponseEntity<List<Order>> getAllOrdersAdmin() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    // ===================== ADMIN: GET ORDER BY ID ===================
    @GetMapping("/admin/{orderId}")
    public ResponseEntity<Order> getOrderByIdAdmin(
            @PathVariable Long orderId
    ) {
        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }

    // ===================== ADMIN: UPDATE ORDER STATUS ===================
    @PutMapping("/admin/{id}/status")
    public ResponseEntity<Order> updateOrderStatusAdmin(
            @PathVariable Long id,
            @RequestBody Map<String, String> request
    ) {
        String newStatus = request.get("orderStatus");
        Order order = orderService.updateOrderStatus(id, newStatus);
        return ResponseEntity.ok(order);
    }
}

package com.Ecom.service;

import com.Ecom.exception.ResourceNotFoundException;
import com.Ecom.model.Order;
import com.Ecom.model.Payment;
import com.Ecom.model.User;
import com.Ecom.repository.OrderRepository;
import com.Ecom.repository.PaymentRepository;
import com.Ecom.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    // Process payment for an order
    public Payment processPayment(Long orderId, String userEmail, String paymentMethod) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Access denied for this order");
        }

        // Find existing payment or create new
        Payment payment = paymentRepository.findByOrder(order)
                .orElse(Payment.builder()
                        .order(order)
                        .amount(order.getTotalAmount())
                        .paymentDate(LocalDateTime.now())
                        .build());

        payment.setPaymentMethod(paymentMethod);
        payment.setPaymentStatus("SUCCESS"); // For simplicity, assume payment is successful
        payment.setPaymentDate(LocalDateTime.now());

        return paymentRepository.save(payment);
    }

    // Get payment by order
    public Payment getPaymentByOrder(Long orderId, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Access denied for this order");
        }

        return paymentRepository.findByOrder(order)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found for this order"));
    }

    // Get all payments for a user
    public List<Payment> getPaymentsByUser(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return user.getCart() != null ? paymentRepository.findAll() : List.of();
        // Optional: Can customize to fetch payments related only to user's orders
    }
}

package com.Ecom.service;

import java.time.LocalDateTime;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Ecom.exception.ResourceNotFoundException;
import com.Ecom.model.Order;
import com.Ecom.model.Payment;
import com.Ecom.model.User;
import com.Ecom.repository.OrderRepository;
import com.Ecom.repository.PaymentRepository;
import com.Ecom.repository.UserRepository;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

import jakarta.annotation.PostConstruct;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    private RazorpayClient client;

    @PostConstruct
    public void init() throws RazorpayException {
        client = new RazorpayClient("rzp_test_RmEi7bwQyWNCUh", "1CAaXg25rtcBkClxyUqXsBba");
    }

    // Process normal COD/UPI payments
    public Payment processPayment(Long orderId, String userEmail, String paymentMethod) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Access denied for this order");
        }

        Payment payment = paymentRepository.findByOrder(order)
                .orElse(Payment.builder()
                        .order(order)
                        .amount(order.getTotalAmount())
                        .paymentDate(LocalDateTime.now())
                        .build());

        payment.setPaymentMethod(paymentMethod);
        payment.setPaymentStatus("SUCCESS");
        payment.setPaymentDate(LocalDateTime.now());

        return paymentRepository.save(payment);
    }

    // Razorpay Order Creation
    public com.razorpay.Order createOrder(double amount) throws RazorpayException {
        
        JSONObject options = new JSONObject();
        options.put("amount", (int)(amount * 100));  
        options.put("currency", "INR");
        options.put("payment_capture", 1);

        return client.orders.create(options);
    }

    // Payment History
    public List<Payment> getPaymentsByUser(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return paymentRepository.findAll();
    }
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

}

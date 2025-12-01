package com.Ecom.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.Ecom.dto.PaymentRequest;
import com.Ecom.model.Payment;
import com.Ecom.service.PaymentService;

@RestController
@RequestMapping("/payments")
@CrossOrigin(origins = "*")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;


    // ==================== PROCESS PAYMENT ====================
    @PostMapping("/process")
    public ResponseEntity<Payment> processPayment(
            Authentication authentication,
            @RequestBody PaymentRequest request) {

        String email = authentication.getName();

        Payment payment = paymentService.processPayment(
                request.getOrderId(),
                email,
                request.getPaymentMethod()
        );

        return ResponseEntity.ok(payment);
    }


    // ==================== GET PAYMENT BY ORDER ====================
    @GetMapping("/order/{orderId}")
    public ResponseEntity<Payment> getPaymentForOrder(
            Authentication authentication,
            @PathVariable Long orderId) {

        String email = authentication.getName();
        Payment payment = paymentService.getPaymentByOrder(orderId, email);

        return ResponseEntity.ok(payment);
    }


    // ==================== GET ALL PAYMENTS FOR USER ====================
    @GetMapping
    public ResponseEntity<List<Payment>> getUserPayments(Authentication authentication) {
        String email = authentication.getName();
        List<Payment> payments = paymentService.getPaymentsByUser(email);

        return ResponseEntity.ok(payments);
    }


    // ==================== CREATE RAZORPAY ORDER ====================
    @PostMapping("/create-order")
    public ResponseEntity<?> createRazorpayOrder(@RequestBody Map<String, Object> data) {
        try {
            double amount = Double.parseDouble(data.get("amount").toString());

            // IMPORTANT: use fully-qualified Razorpay Order here
            com.razorpay.Order razorpayOrder = paymentService.createOrder(amount);

            return ResponseEntity.ok(razorpayOrder.toJson().toString());

        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}

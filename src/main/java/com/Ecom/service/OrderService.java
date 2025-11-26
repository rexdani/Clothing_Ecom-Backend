package com.Ecom.service;

import com.Ecom.dto.PlaceOrderRequest;
import com.Ecom.exception.ResourceNotFoundException;
import com.Ecom.model.*;
import com.Ecom.repository.CartRepository;
import com.Ecom.repository.OrderRepository;
import com.Ecom.repository.PaymentRepository;
import com.Ecom.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    // Place a new order
    @Transactional
    public Order placeOrder(PlaceOrderRequest request, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        if (cart.getItems().isEmpty()) {
            throw new IllegalArgumentException("Cart is empty");
        }

        Order order = new Order();
        order.setUser(user);
        order.setAddress(user.getAddress()); // or fetch by request.getAddressId()
        order.setOrderStatus("PENDING");
        order.setOrderDate(LocalDateTime.now());

        List<OrderItem> orderItems = new ArrayList<>();
        double totalAmount = 0;

        for (CartItem cartItem : cart.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getPrice());
            orderItems.add(orderItem);

            totalAmount += cartItem.getQuantity() * cartItem.getPrice();
        }

        order.setItems(orderItems);
        order.setTotalAmount(totalAmount);

        // Save order
        Order savedOrder = orderRepository.save(order);

        // Optionally create payment record for order
        Payment payment = Payment.builder()
                .order(savedOrder)
                .paymentMethod(request.getPaymentMethod())
                .paymentStatus("PENDING")
                .amount(totalAmount)
                .paymentDate(LocalDateTime.now())
                .build();

        paymentRepository.save(payment);

        // Clear user's cart
        cart.getItems().clear();
        cart.calculateTotalPrice();
        cartRepository.save(cart);

        return savedOrder;
    }

    // Get all orders for a user
    public List<Order> getOrdersByUser(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return orderRepository.findByUser(user);
    }

    // Get order by ID
    public Order getOrderById(Long orderId, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Access denied for this order");
        }

        return order;
    }
}

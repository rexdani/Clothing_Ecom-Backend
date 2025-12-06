package com.Ecom.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Ecom.dto.PlaceOrderRequest;
import com.Ecom.exception.ResourceNotFoundException;
import com.Ecom.model.Cart;
import com.Ecom.model.CartItem;
import com.Ecom.model.Order;
import com.Ecom.model.OrderItem;
import com.Ecom.model.Payment;
import com.Ecom.model.User;
import com.Ecom.repository.CartRepository;
import com.Ecom.repository.OrderRepository;
import com.Ecom.repository.PaymentRepository;
import com.Ecom.repository.UserRepository;

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
        order.setDeliveryDate(LocalDateTime.now().plusDays(7)); // Estimated delivery date
        order.setPayment(request.getPaymentMethod()); // Will be set after payment is processed

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
 // Get all orders (admin only)
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // Get order by ID (admin - no user check)
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
            .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    }

    public Order updateOrderStatus(Long id, String newStatus) {

        // Find order
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        String currentStatus = order.getOrderStatus();

        // Define allowed transitions
        List<String> allowedStatuses = List.of("PENDING", "CONFIRMED", "SHIPPED", "DELIVERED", "CANCELLED");

        if (!allowedStatuses.contains(newStatus)) {
            throw new IllegalArgumentException("Invalid order status: " + newStatus);
        }

        // Business rules
        if (currentStatus.equals("DELIVERED")) {
            throw new IllegalArgumentException("Order is already delivered and cannot be changed");
        }

        if (currentStatus.equals("CANCELLED")) {
            throw new IllegalArgumentException("Order is cancelled and cannot be updated");
        }

        // Update status
        order.setOrderStatus(newStatus);

        // Update payment if needed
        Payment payment = paymentRepository.findByOrder(order).orElse(null);

        if (payment != null) {
            switch (newStatus) {
                case "CONFIRMED":
                    payment.setPaymentStatus("PROCESSING");
                    break;

                case "SHIPPED":
                    payment.setPaymentStatus("PROCESSING");
                    break;

                case "DELIVERED":
                    payment.setPaymentStatus("PAID");
                    break;

                case "CANCELLED":
                    payment.setPaymentStatus("REFUNDED");
                    break;
            }
            paymentRepository.save(payment);
        }

        return orderRepository.save(order);
    }

}

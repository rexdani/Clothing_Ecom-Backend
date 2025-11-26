package com.Ecom.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Ecom.dto.AddToCartRequest;
import com.Ecom.exception.ResourceNotFoundException;
import com.Ecom.model.Cart;
import com.Ecom.model.CartItem;
import com.Ecom.model.Product;
import com.Ecom.model.User;
import com.Ecom.repository.CartItemRepository;
import com.Ecom.repository.CartRepository;
import com.Ecom.repository.ProductRepository;
import com.Ecom.repository.UserRepository;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartItemRepository cartItemRepository;

    // Add product to user's cart
    @Transactional
    public Cart addToCart(AddToCartRequest request, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElse(Cart.builder().user(user).build());

        // Check if product already in cart
        CartItem existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + request.getQuantity());
        } else {
            CartItem newItem = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(request.getQuantity())
                    .price(product.getPrice())
                    .build();
            cart.getItems().add(newItem);
        }

        cart.calculateTotalPrice();
        return cartRepository.save(cart);
    }

    // Remove product from cart
    @Transactional
    public Cart removeFromCart(Long cartItemId, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        CartItem itemToRemove = cart.getItems().stream()
                .filter(item -> item.getId().equals(cartItemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));

        cart.getItems().remove(itemToRemove);
        cart.calculateTotalPrice();

        return cartRepository.save(cart);
    }

    // Get user's cart
    public Cart getCart(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return cartRepository.findByUser(user)
                .orElse(Cart.builder().user(user).build());
    }

    // Clear user's cart
    @Transactional
    public Cart clearCart(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        cart.getItems().clear();
        cart.calculateTotalPrice();

        return cartRepository.save(cart);
    }

    // Update quantity for a cart item (if quantity < 1 the item is removed)
    @Transactional
    public Cart updateCartItemQuantity(Long cartItemId, int quantity, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        CartItem item = cart.getItems().stream()
                .filter(i -> i.getId().equals(cartItemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));

        if (quantity < 1) {
            cart.getItems().remove(item);
        } else {
            item.setQuantity(quantity);
        }

        cart.calculateTotalPrice();
        return cartRepository.save(cart);
    }
    public int getCartCount(User user) {
        Cart cart = cartRepository.findByUser(user)
                        .orElseThrow(() -> new RuntimeException("Cart not found"));
        return cartItemRepository.countByCart(cart);
    }
    
}
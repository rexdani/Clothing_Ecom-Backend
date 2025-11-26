package com.Ecom.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Ecom.dto.AddToCartRequest;
import com.Ecom.model.Cart;
import com.Ecom.model.User;
import com.Ecom.service.CartService;

@RestController
@RequestMapping("/cart")
@CrossOrigin(origins = "*")
public class CartController {

    @Autowired
    private CartService cartService;

    // ================== GET USER CART ==================
    @GetMapping
    public ResponseEntity<Cart> getCart(Authentication authentication) {
        String email = authentication.getName();
        Cart cart = cartService.getCart(email);
        return ResponseEntity.ok(cart);
    }

    // ================== ADD TO CART ==================
    @PostMapping("/add")
    public ResponseEntity<Cart> addToCart(
            Authentication authentication,
            @RequestBody AddToCartRequest request
    ) {
        String email = authentication.getName();
        Cart cart = cartService.addToCart(request, email);
        return ResponseEntity.ok(cart);
    }

    // ================== REMOVE ITEM FROM CART ==================
    @DeleteMapping("/remove/{cartItemId}")
    public ResponseEntity<String> removeItem(
            Authentication authentication,
            @PathVariable Long cartItemId
    ) {
        String email = authentication.getName();
        cartService.removeFromCart(cartItemId, email);
        return ResponseEntity.ok("Item removed successfully");
    }

    // ================== CLEAR CART ==================
    @DeleteMapping("/clear")
    public ResponseEntity<String> clearCart(Authentication authentication) {
        String email = authentication.getName();
        cartService.clearCart(email);
        return ResponseEntity.ok("Cart cleared successfully");
    }
    @GetMapping("/count/{userId}")
    public ResponseEntity<Integer> getCartCount(@PathVariable User userId) {
        int count = cartService.getCartCount(userId);
        return ResponseEntity.ok(count);
    }
    
    // ================== UPDATE CART ITEM QUANTITY ==================
    @PutMapping("/update/{cartItemId}")
    public ResponseEntity<Cart> updateCartItemQuantity(
            Authentication authentication,
            @PathVariable Long cartItemId,
            @RequestBody Map<String, Integer> body
    ) {
        String email = authentication.getName();
        Integer quantity = body.get("quantity");
        if (quantity == null) {
            return ResponseEntity.badRequest().build();
        }
        Cart cart = cartService.updateCartItemQuantity(cartItemId, quantity, email);
        return ResponseEntity.ok(cart);
    }
    
}
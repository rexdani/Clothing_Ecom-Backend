package com.Ecom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Ecom.dto.AddToWishlistRequest;
import com.Ecom.model.Wishlist;
import com.Ecom.service.WishlistService;

@RestController
@RequestMapping("/wishlist")
@CrossOrigin(origins = "*")
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    @GetMapping
    public ResponseEntity<?> getWishlist(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("User not authenticated");
        }

        String email = authentication.getName();
        Wishlist wishlist = wishlistService.getWishlist(email);
        return ResponseEntity.ok(wishlist);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addToWishlist(
            Authentication authentication,
            @RequestBody AddToWishlistRequest request) {

        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("User not authenticated");
        }

        String email = authentication.getName();
        Wishlist wishlist = wishlistService.addToWishlist(request, email);
        return ResponseEntity.ok(wishlist);
    }

    @DeleteMapping("/remove/{wishlistItemId}")
    public ResponseEntity<?> removeFromWishlist(
            Authentication authentication,
            @PathVariable Long wishlistItemId) {
        
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("User not authenticated");
        }

        String email = authentication.getName();
        wishlistService.removeFromWishlist(wishlistItemId, email);
        return ResponseEntity.ok("Item removed from wishlist");
    }

    @DeleteMapping("/clear")
    public ResponseEntity<?> clearWishlist(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("User not authenticated");
        }

        String email = authentication.getName();
        wishlistService.clearWishlist(email);
        return ResponseEntity.ok("Wishlist cleared");
    }
}


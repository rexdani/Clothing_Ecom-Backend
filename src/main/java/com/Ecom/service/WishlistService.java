package com.Ecom.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Ecom.dto.AddToWishlistRequest;
import com.Ecom.exception.ResourceNotFoundException;
import com.Ecom.model.Product;
import com.Ecom.model.User;
import com.Ecom.model.Wishlist;
import com.Ecom.model.WishlistItem;
import com.Ecom.repository.ProductRepository;
import com.Ecom.repository.UserRepository;
import com.Ecom.repository.WishlistItemRepository;
import com.Ecom.repository.WishlistRepository;

@Service
public class WishlistService {

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private WishlistItemRepository wishlistItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    // Get or create wishlist for user
    public Wishlist getWishlist(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return wishlistRepository.findByUser(user).orElse(Wishlist.builder().user(user).build());
    }

    // Add product to wishlist (idempotent)
    @Transactional
    public Wishlist addToWishlist(AddToWishlistRequest request, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        Wishlist wishlist = wishlistRepository.findByUser(user)
                .orElse(Wishlist.builder().user(user).build());

        // check duplicate
        boolean exists = wishlist.getItems().stream()
                .anyMatch(item -> item.getProduct().getId().equals(product.getId()));

        if (!exists) {
            WishlistItem item = WishlistItem.builder()
                    .wishlist(wishlist)
                    .product(product)
                    .build();
            wishlist.getItems().add(item);
            wishlist = wishlistRepository.save(wishlist);
        }

        return wishlist;
    }

    // Remove wishlist item
    @Transactional
    public void removeFromWishlist(Long wishlistItemId, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Wishlist wishlist = wishlistRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Wishlist not found"));

        WishlistItem toRemove = wishlist.getItems().stream()
                .filter(i -> i.getId().equals(wishlistItemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Wishlist item not found"));

        wishlist.getItems().remove(toRemove);
        wishlistRepository.save(wishlist);
    }

    // Clear wishlist
    @Transactional
    public void clearWishlist(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Wishlist wishlist = wishlistRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Wishlist not found"));

        wishlist.getItems().clear();
        wishlistRepository.save(wishlist);
    }

    public int getWishlistCount(User user) {
        Wishlist wishlist = wishlistRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Wishlist not found"));
        return wishlistItemRepository.countByWishlist(wishlist);
    }
}

package com.Ecom.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Ecom.model.Wishlist;
import com.Ecom.model.WishlistItem;

public interface WishlistItemRepository extends JpaRepository<WishlistItem, Long> {
    int countByWishlist(Wishlist wishlist);
}

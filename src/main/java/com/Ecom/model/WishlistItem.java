package com.Ecom.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "wishlist_items")
@JsonIgnoreProperties({"wishlist"})
public class WishlistItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "wishlist_id")
    @JsonIgnoreProperties({"items", "user"})
    private Wishlist wishlist;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonIgnoreProperties({"cartItems"})
    private Product product;

    public WishlistItem() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Wishlist getWishlist() { return wishlist; }
    public void setWishlist(Wishlist wishlist) { this.wishlist = wishlist; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public static WishlistItemBuilder builder() { return new WishlistItemBuilder(); }

    public static class WishlistItemBuilder {
        private Long id;
        private Wishlist wishlist;
        private Product product;

        public WishlistItemBuilder id(Long id) { this.id = id; return this; }
        public WishlistItemBuilder wishlist(Wishlist wishlist) { this.wishlist = wishlist; return this; }
        public WishlistItemBuilder product(Product product) { this.product = product; return this; }

        public WishlistItem build() {
            WishlistItem item = new WishlistItem();
            item.setId(this.id);
            item.setWishlist(this.wishlist);
            item.setProduct(this.product);
            return item;
        }
    }
}

package com.Ecom.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "cart_items")
@JsonIgnoreProperties({"cart"}) // prevents infinite loop from Cart → CartItem → Cart
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    @JsonIgnoreProperties({"items", "user"})   
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonIgnoreProperties({"cartItems"})  // prevent reverse loop Product → CartItem → Product
    private Product product;

    private int quantity;
    private double price;

    public CartItem() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Cart getCart() { return cart; }
    public void setCart(Cart cart) { this.cart = cart; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public static CartItemBuilder builder() {
        return new CartItemBuilder();
    }

    public static class CartItemBuilder {
        private Long id;
        private Cart cart;
        private Product product;
        private int quantity;
        private double price;

        public CartItemBuilder id(Long id) { this.id = id; return this; }
        public CartItemBuilder cart(Cart cart) { this.cart = cart; return this; }
        public CartItemBuilder product(Product product) { this.product = product; return this; }
        public CartItemBuilder quantity(int quantity) { this.quantity = quantity; return this; }
        public CartItemBuilder price(double price) { this.price = price; return this; }

        public CartItem build() {
            CartItem item = new CartItem();
            item.setId(this.id);
            item.setCart(this.cart);
            item.setProduct(this.product);
            item.setQuantity(this.quantity);
            item.setPrice(this.price);
            return item;
        }
    }
}

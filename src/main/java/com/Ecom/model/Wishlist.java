package com.Ecom.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;

@Entity
@Table(name = "wishlists")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Wishlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonManagedReference("user-wishlist")
    private User user;

    @OneToMany(mappedBy = "wishlist", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WishlistItem> items = new ArrayList<>();

    public Wishlist() {}

    // getters & setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public List<WishlistItem> getItems() { return items; }
    public void setItems(List<WishlistItem> items) { this.items = items; }

    // Manual builder
    public static WishlistBuilder builder() { return new WishlistBuilder(); }

    public static class WishlistBuilder {
        private Long id;
        private User user;
        private List<WishlistItem> items = new ArrayList<>();

        public WishlistBuilder id(Long id) { this.id = id; return this; }
        public WishlistBuilder user(User user) { this.user = user; return this; }
        public WishlistBuilder items(List<WishlistItem> items) { this.items = items; return this; }

        public Wishlist build() {
            Wishlist w = new Wishlist();
            w.setId(this.id);
            w.setUser(this.user);
            w.setItems(this.items);
            return w;
        }
    }
}

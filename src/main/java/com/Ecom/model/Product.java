package com.Ecom.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private Double price;

    private Integer stock;


    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] image;     // Stores actual image bytes (BLOB)

    @JsonIgnoreProperties("products")
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    public Product() {}

    // ---------------- GETTERS & SETTERS ----------------

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }


    public byte[] getImage() { return image; }
    public void setImage(byte[] image) { this.image = image; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    // ---------------- MANUAL BUILDER ----------------

    public static ProductBuilder builder() { return new ProductBuilder(); }

    public static class ProductBuilder {

        private Long id;
        private String name;
        private String description;
        private Double price;
        private Integer stock;
        private byte[] image;
        private Category category;

        public ProductBuilder id(Long id) {
            this.id = id; return this;
        }

        public ProductBuilder name(String name) {
            this.name = name; return this;
        }

        public ProductBuilder description(String description) {
            this.description = description; return this;
        }

        public ProductBuilder price(Double price) {
            this.price = price; return this;
        }

        public ProductBuilder stock(Integer stock) {
            this.stock = stock; return this;
        }

        public ProductBuilder image(byte[] image) {
            this.image = image; return this;
        }

        public ProductBuilder category(Category category) {
            this.category = category; return this;
        }

        public Product build() {
            Product product = new Product();
            product.setId(this.id);
            product.setName(this.name);
            product.setDescription(this.description);
            product.setPrice(this.price);
            product.setStock(this.stock);
            product.setImage(this.image);
            product.setCategory(this.category);
            return product;
        }
    }
}

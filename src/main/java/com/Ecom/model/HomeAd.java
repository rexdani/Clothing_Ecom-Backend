package com.Ecom.model;

import jakarta.persistence.*;

@Entity
@Table(name = "home_ads")
public class HomeAd {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String type;
    private String redirectUrl;

    @Lob
    private byte[] image;

    @Transient
    private String imageBase64;

    private boolean active = true;

    // -----------------------------
    // MANUAL BUILDER
    // -----------------------------
    public static class Builder {
        private Long id;
        private String title;
        private String type;
        private String redirectUrl;
        private byte[] image;
        private String imageBase64;
        private boolean active;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder redirectUrl(String redirectUrl) {
            this.redirectUrl = redirectUrl;
            return this;
        }

        public Builder image(byte[] image) {
            this.image = image;
            return this;
        }

        public Builder imageBase64(String base64) {
            this.imageBase64 = base64;
            return this;
        }

        public Builder active(boolean active) {
            this.active = active;
            return this;
        }

        public HomeAd build() {
            return new HomeAd(id, title, type, redirectUrl, image, imageBase64, active);
        }
    }

    // Expose builder
    public static Builder builder() {
        return new Builder();
    }

    // -----------------------------
    // CONSTRUCTORS
    // -----------------------------
    public HomeAd() {}

    public HomeAd(Long id, String title, String type, String redirectUrl, byte[] image,
                  String imageBase64, boolean active) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.redirectUrl = redirectUrl;
        this.image = image;
        this.imageBase64 = imageBase64;
        this.active = active;
    }

    // -----------------------------
    // GETTERS & SETTERS
    // -----------------------------
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getRedirectUrl() { return redirectUrl; }
    public void setRedirectUrl(String redirectUrl) { this.redirectUrl = redirectUrl; }

    public byte[] getImage() { return image; }
    public void setImage(byte[] image) { this.image = image; }

    public String getImageBase64() { return imageBase64; }
    public void setImageBase64(String imageBase64) { this.imageBase64 = imageBase64; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}

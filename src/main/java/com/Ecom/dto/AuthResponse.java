package com.Ecom.dto;

import java.util.Set;
import com.Ecom.model.Role;

public class AuthResponse {

    private String token;
    private String message;
    private Long userId;
    private String email;
    private Set<Role> roles;

    // ---------- Constructors ----------
    public AuthResponse() {}

    public AuthResponse(String token, String message, Long userId, String email, Set<Role> roles) {
        this.token = token;
        this.message = message;
        this.userId = userId;
        this.email = email;
        this.roles = roles;
    }

    // ---------- Getters & Setters ----------
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Set<Role> getRoles() { return roles; }
    public void setRoles(Set<Role> roles) { this.roles = roles; }

    // ---------- MANUAL BUILDER ----------
    public static class Builder {
        private String token;
        private String message;
        private Long userId;
        private String email;
        private Set<Role> roles;

        public Builder token(String token) {
            this.token = token;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder roles(Set<Role> roles) {
            this.roles = roles;
            return this;
        }

        public AuthResponse build() {
            return new AuthResponse(token, message, userId, email, roles);
        }
    }

    // Helper method
    public static Builder builder() {
        return new Builder();
    }
}

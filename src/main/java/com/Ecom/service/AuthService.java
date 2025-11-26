package com.Ecom.service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.Ecom.dto.AuthRequest;
import com.Ecom.dto.AuthResponse;
import com.Ecom.model.Role;
import com.Ecom.model.User;
import com.Ecom.repository.RoleRepository;
import com.Ecom.repository.UserRepository;
import com.Ecom.util.JwtUtil;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    // ------------------ REGISTER ------------------
    public AuthResponse register(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("ROLE_USER not found"));

        user.setRoles(Set.of(userRole));
        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getEmail());

        return AuthResponse.builder()
                .token(token)
                .message("User registered successfully")
                .userId(user.getId())
                .email(user.getEmail())
                .roles(user.getRoles())
                .build();
    }

    // ------------------ NORMAL LOGIN ------------------
    public AuthResponse login(AuthRequest authRequest) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getEmail(),
                        authRequest.getPassword()
                )
        );

        User user = userRepository.findByEmail(authRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtUtil.generateToken(user.getEmail());

        return AuthResponse.builder()
                .token(token)
                .message("Login successful")
                .userId(user.getId())
                .email(user.getEmail())
                .roles(user.getRoles())
                .build();
    }

    // ------------------ GOOGLE LOGIN ------------------
    public AuthResponse googleLogin(String idTokenString) {
        try {

            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(),
                    new GsonFactory()
            )
            .setAudience(Collections.singletonList("856313994821-qqi10amq812emvt5q2tgo9otkpf2e21u.apps.googleusercontent.com"))
            .build();

            GoogleIdToken idToken = verifier.verify(idTokenString);

            if (idToken == null) {
                throw new RuntimeException("Invalid Google token");
            }

            GoogleIdToken.Payload payload = idToken.getPayload();

            String email = payload.getEmail();
            String name = (String) payload.get("name");

            // Check if user exists
            User user = userRepository.findByEmail(email).orElse(null);

            if (user == null) {
                // Auto-register Google user
                user = new User();
                user.setEmail(email);
                user.setName(name);
                user.setPassword(passwordEncoder.encode("GOOGLE_AUTH"));

                Role role = roleRepository.findByName("ROLE_USER")
                        .orElseThrow(() -> new RuntimeException("ROLE_USER not found"));

                user.setRoles(Set.of(role));
                userRepository.save(user);
            }

            String jwtToken = jwtUtil.generateToken(email);

            return AuthResponse.builder()
                    .token(jwtToken)
                    .message("Google login success")
                    .userId(user.getId())
                    .email(user.getEmail())
                    .roles(user.getRoles())
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("Google login failed: " + e.getMessage());
        }
    }
}
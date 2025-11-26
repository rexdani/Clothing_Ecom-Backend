package com.Ecom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Ecom.dto.AuthRequest;
import com.Ecom.dto.AuthResponse;
import com.Ecom.dto.GoogleLoginRequest;
import com.Ecom.dto.RegisterRequest;
import com.Ecom.model.Address;
import com.Ecom.model.User;
import com.Ecom.repository.UserRepository;
import com.Ecom.service.AuthService;
import com.Ecom.util.JwtUtil;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;   // ✅ FIX: Add missing repository

    // -------------------- LOGIN -------------------------
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {

        // Authenticate user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // Load user from DB
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Generate JWT Token
        String token = jwtUtil.generateToken(request.getEmail());

        // Build response
        AuthResponse response = AuthResponse.builder()
                .token(token)
                .message("Login successful")
                .userId(user.getId())
                .email(user.getEmail())
                .roles(user.getRoles())
                .build();

        return ResponseEntity.ok(response);
    }

    // -------------------- REGISTER -------------------------
    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest request) {

        User user = new User();
        user.setName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setPhone(request.getPhone());

        Address address = new Address();
        address.setFullName(request.getAddress().getFullName());
        address.setPhoneNumber(request.getAddress().getPhoneNumber());
        address.setStreet(request.getAddress().getStreet());
        address.setCity(request.getAddress().getCity());
        address.setState(request.getAddress().getState());
        address.setCountry(request.getAddress().getCountry());
        address.setPostalCode(request.getAddress().getPostalCode());

        user.setAddress(address);

        return authService.register(user);
    }
    @PostMapping("/google")
    public ResponseEntity<?> googleLogin(@RequestBody GoogleLoginRequest request) {

        String idToken = request.getIdToken();

        AuthResponse response = authService.googleLogin(idToken);

        return ResponseEntity.ok(response);
    }
}

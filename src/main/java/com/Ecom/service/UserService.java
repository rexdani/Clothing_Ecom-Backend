package com.Ecom.service;

import com.Ecom.exception.ResourceNotFoundException;
import com.Ecom.model.User;
import com.Ecom.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ===================== FIND BY EMAIL ======================
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    // ===================== SAVE USER ======================
    public User save(User user) {
        return userRepository.save(user);
    }

    // ===================== GET ALL USERS ======================
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // ===================== GET USER BY ID ======================
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
    }

    // ===================== UPDATE USER ======================
    public User updateUser(Long userId, User userDetails) {
        User user = getUserById(userId);

        user.setName(userDetails.getName());
        user.setPhone(userDetails.getPhone());
        user.setEmail(userDetails.getEmail());
        user.setAddress(userDetails.getAddress());

        // update password if provided
        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        }

        return userRepository.save(user);
    }

    // ===================== DELETE USER ======================
    public void deleteUser(Long userId) {
        User user = getUserById(userId);
        userRepository.delete(user);
    }
}

package com.Ecom.service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.Ecom.exception.ResourceNotFoundException;
import com.Ecom.model.Role;
import com.Ecom.model.User;
import com.Ecom.repository.RoleRepository;
import com.Ecom.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;

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
    public User updateUserRoles(Long userId, List<String> roleNames) {

        // 1. Validate user exists
        User user = getUserById(userId);
        if (user == null) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }

        // 2. Validate roleNames list
        if (roleNames == null || roleNames.isEmpty()) {
            throw new IllegalArgumentException("Role names list cannot be empty");
        }

        // 3. Convert role names to Role entities
        Set<Role> roles = roleNames.stream()
            .filter(Objects::nonNull) // avoid null role names
            .map(roleName -> roleRepository.findByName(roleName)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + roleName)))
            .collect(Collectors.toSet());

        // 4. Update and save user roles
        user.setRoles(roles);
        return userRepository.save(user);
    }

}

package com.Ecom.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Ecom.dto.UpdateUserRequest;
import com.Ecom.model.User;
import com.Ecom.repository.UserRepository;
import com.Ecom.service.UserService;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private UserRepository userRepository;
	// ================= GET PROFILE ====================
	@GetMapping("/profile")
	public ResponseEntity<User> getUserProfile(Authentication authentication) {

		String email = authentication.getName();
		User user = userService.findByEmail(email);

		if (user == null) {
			return ResponseEntity.notFound().build();
		}

		user.setPassword(null); // hide password
		return ResponseEntity.ok(user);
	}

	// ================= UPDATE PROFILE ====================
	@PutMapping(value = "/profile", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> updateUserProfile(
	        Authentication authentication,
	        @RequestBody UpdateUserRequest request ) {

	    String email = authentication.getName();
	    User user = userService.findByEmail(email);

	    if (user == null) {
	        return ResponseEntity.notFound().build();
	    }

	    // Update allowed fields only
	    user.setName(request.getName());
	    user.setEmail(request.getEmail());
	    user.setPhone(request.getPhone());
	    user.setAddress(request.getAddress());

	    User savedUser = userService.save(user);
	    savedUser.setPassword(null); // hide password

	    return ResponseEntity.ok(savedUser);
	}
	@GetMapping("/admin/all")
	public ResponseEntity<?> getAllUsers() {
		try {
			List<User> users = userRepository.findAll();
			return ResponseEntity.ok(users);
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Error fetching users: " + e.getMessage());
		}
	}

}

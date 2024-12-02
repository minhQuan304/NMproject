package com.example.NMproject.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.NMproject.dto.ApiResponse;
import com.example.NMproject.dto.RegisterRequest;
import com.example.NMproject.dto.UserResponse;
import com.example.NMproject.entity.UserEntity;
import com.example.NMproject.service.UserService;

@Controller
@RequestMapping("/api/auth")
public class UserController {
	@Autowired
	private UserService userService;

	@GetMapping("/login")
	public String loginPage() {
		return "loginRegister"; // Trả về file loginRegister.html trong thư mục templates
	}

	// Đăng nhập người dùng bằng email
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody RegisterRequest request) {
		Optional<UserEntity> user = userService.loginUser(request.getEmail(), request.getPassword()); // Dùng email thay
																										// cho username
		return user.map(u -> {
			UserResponse userResponse = new UserResponse(u.getId(), u.getEmail()); // Trả về email thay vì username
			return ResponseEntity.ok(new ApiResponse("User login successfully", userResponse));
		}).orElseGet(() -> {
			return ResponseEntity.status(401).body(new ApiResponse("Invalid email or password"));
		});
	}

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
		try {
			UserEntity newUser = userService.registerUser(request.getEmail(), request.getUsername(),
					request.getPassword()); // Truyền thêm username
			UserResponse userResponse = new UserResponse(newUser.getId(), newUser.getEmail());
			return ResponseEntity.ok(new ApiResponse("User registered successfully", userResponse));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new ApiResponse("Error: " + e.getMessage()));
		}
	}
}

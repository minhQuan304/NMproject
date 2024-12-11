package com.example.NMproject.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.NMproject.dto.AccountResponse;
import com.example.NMproject.dto.ApiResponse;
import com.example.NMproject.dto.RegisterRequest;
import com.example.NMproject.entity.AccountEntity;
import com.example.NMproject.service.AccountService;

@Controller
@RequestMapping("/api/auth")
public class AccountController {
	@Autowired
	private AccountService userService;

	@GetMapping("/login")
	public String loginPage() {
		return "loginRegister"; // Trả về file loginRegister.html trong thư mục templates
	}

	// Đăng nhập người dùng bằng email
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody RegisterRequest request) {
		Optional<AccountEntity> user = userService.loginUser(request.getEmail(), request.getPassword());
		return user.map(u -> {
			// Thêm avatarUrl vào trong response
			AccountResponse userResponse = new AccountResponse(u.getId(), u.getEmail(), u.getUsername());
			return ResponseEntity.ok(new ApiResponse("User login successfully", userResponse));
		}).orElseGet(() -> {
			return ResponseEntity.status(401).body(new ApiResponse("Invalid email or password"));
		});
	}

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
		try {
			// Đăng ký người dùng mới
			AccountEntity newUser = userService.registerUser(request.getEmail(), request.getUsername(),
					request.getPassword());

			// Trả về đối tượng AccountResponse với đầy đủ thông tin
			AccountResponse userResponse = new AccountResponse(newUser.getId(), newUser.getEmail(),
					newUser.getPassword(), newUser.getUsername());

			// Trả về phản hồi thành công
			return ResponseEntity.ok(new ApiResponse("User registered successfully", userResponse));
		} catch (Exception e) {
			// Xử lý lỗi nếu có
			return ResponseEntity.badRequest().body(new ApiResponse("Error: " + e.getMessage()));
		}
	}

}

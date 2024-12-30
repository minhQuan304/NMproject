package com.example.NMproject.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

	// Trả về trang loginRegister (giả sử bạn dùng Thymeleaf hoặc JSP)
	@GetMapping("/login")
	public String loginPage() {
		return "loginRegister"; // Trả về file loginRegister.html trong thư mục templates
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody RegisterRequest request) {
		Optional<AccountResponse> user = userService.loginUser(request.getEmail(), request.getPassword());

		return user
				.map(u -> ResponseEntity.ok(Map.of("message", "User login successfully", "userID", u.getUserID(),
						"username", u.getUsername(), "imageLink", u.getImageLink(), "name", u.getName(), "phone",
						u.getPhone(), "address", u.getAddress(), "userRole", u.getUserRole()))) // Thêm
																								// userRole
																								// vào
				// phản hồi
				.orElseGet(() -> ResponseEntity.status(401).body(Map.of("message", "Invalid email or password")));
	}

	// Đăng nhập người dùng bằng email và password
//	@PostMapping("/login")
//	public ResponseEntity<?> login(@RequestBody RegisterRequest request) {
//		Optional<AccountEntity> user = userService.loginUser(request.getEmail(), request.getPassword());
//		return user.map(u -> {
//			// Tạo AccountResponse với đầy đủ thông tin, bao gồm cả avatarUrl và userRole
//			AccountResponse userResponse = new AccountResponse(u.getUserID(), u.getEmail(), u.getUsername(),
//					u.getName(), u.getPhone(), u.getAddress(), u.getUserRole() // Lấy userRole từ AccountEntity
//			);
//			return ResponseEntity.ok(userResponse); // Trả về thông tin người dùng dưới dạng JSON
//		}).orElseGet(() -> {
//			// Khi đăng nhập thất bại, trả về một đối tượng AccountResponse với avatarUrl
//			// mặc định và userRole là 0
//			AccountResponse errorResponse = new AccountResponse();
//			return ResponseEntity.status(401).body(errorResponse); // Trả về ResponseEntity với lỗi 401 và đối tượng
//																	// AccountResponse rỗng
//		});
//	}

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
		try {
			// Đăng ký người dùng mới
			AccountEntity newUser = userService.registerUser(request.getEmail(), request.getUsername(),
					request.getPassword());

			// Trả về đối tượng AccountResponse với đầy đủ thông tin
			AccountResponse userResponse = new AccountResponse(newUser.getUserID(), newUser.getEmail(),
					newUser.getUsername());

			// Trả về phản hồi thành công
			return ResponseEntity.ok(new ApiResponse("User registered successfully", userResponse));
		} catch (Exception e) {
			// Xử lý lỗi nếu có
			return ResponseEntity.badRequest().body(new ApiResponse("Error: " + e.getMessage()));
		}
	}

	// Lấy tất cả người dùng với thông tin tài khoản
	@GetMapping("/getAll")
	public ResponseEntity<List<AccountResponse>> getAllUsers() {
		List<AccountResponse> users = userService.getAllUsersWithAccountInfo();
		return ResponseEntity.ok(users);
	}

	@DeleteMapping("/delete/{userID}")
	public ResponseEntity<?> deleteUser(@PathVariable("userID") int userID) {
		try {
			// Gọi service để xóa người dùng
			userService.deleteUserById(userID);
			return ResponseEntity.ok(new ApiResponse("User deleted successfully"));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new ApiResponse("Error: " + e.getMessage()));
		}
	}

	@PutMapping("/update")
	public ResponseEntity<Map<String, String>> updateUserProfile(@RequestBody Map<String, Object> payload) {
		long userID = Long.parseLong(payload.get("userID").toString());
		String name = payload.get("name").toString();
		String phone = payload.get("phone").toString();
		String address = payload.get("address").toString();
		try {
			// Gọi service để cập nhật thông tin người dùng
			userService.updateUserProfile(userID, name, phone, address);
			// Trả về thông điệp thành công
			Map<String, String> response = new HashMap<>();
			response.put("message", "User profile updated successfully");
			return ResponseEntity.ok(response);
		} catch (RuntimeException e) {
			// Trả về thông điệp lỗi nếu không tìm thấy người dùng
			Map<String, String> response = new HashMap<>();
			response.put("message", e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
	}

}

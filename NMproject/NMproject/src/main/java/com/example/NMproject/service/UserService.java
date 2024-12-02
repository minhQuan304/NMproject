package com.example.NMproject.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.NMproject.entity.UserEntity;
import com.example.NMproject.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	// Đăng nhập người dùng bằng email và password
	public Optional<UserEntity> loginUser(String email, String password) {
		Optional<UserEntity> user = userRepository.findByEmailAndPassword(email, password);
		if (user.isPresent() && user.get().getPassword().equals(password)) {
			return user;
		}
		return Optional.empty();
	}

	// Đăng ký người dùng mới bằng email, username và password
	public UserEntity registerUser(String email, String username, String password) {
		// Kiểm tra trùng lặp email
		if (userRepository.findByEmail(email).isPresent()) {
			throw new RuntimeException("Email đã tồn tại!");
		}

		// Tạo và lưu người dùng mới
		UserEntity user = new UserEntity(email, username, password); // Thêm username vào constructor
		return userRepository.save(user); // Không cần phải gọi setEmail, setUsername, setPassword nữa
	}
}

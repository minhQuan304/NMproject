package com.example.NMproject.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.NMproject.entity.AccountEntity;
import com.example.NMproject.repository.AccountRepository;

@Service
public class AccountService {

	@Autowired
	private AccountRepository userRepository;

	// Đăng nhập người dùng bằng email và password
	public Optional<AccountEntity> loginUser(String email, String password) {
		Optional<AccountEntity> user = userRepository.findByEmailAndPassword(email, password);
		if (user.isPresent() && user.get().getPassword().equals(password)) {
			return user;
		}
		return Optional.empty();
	}

	// Đăng ký người dùng mới bằng email, username và password
	public AccountEntity registerUser(String email, String username, String password) {
		// Kiểm tra trùng lặp email
		if (userRepository.findByEmail(email).isPresent()) {
			throw new RuntimeException("Email đã tồn tại!");
		}

		// Tạo và lưu người dùng mới
		AccountEntity user = new AccountEntity(email, username, password); // Thêm username vào constructor
		return userRepository.save(user); // Không cần phải gọi setEmail, setUsername, setPassword nữa
	}
}

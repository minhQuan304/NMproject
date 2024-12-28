package com.example.NMproject.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.NMproject.dto.AccountResponse;
import com.example.NMproject.entity.AccountEntity;
import com.example.NMproject.repository.AccountRepository;

@Service
public class AccountService {

	@Autowired
	private AccountRepository accountRepository;

	// Đăng nhập người dùng bằng email và password
	public Optional<AccountResponse> loginUser(String email, String password) {
		return accountRepository.findByEmailAndPassword(email, password)
				.map(user -> new AccountResponse(user.getUserID(), user.getUsername(), user.getEmail(), user.getName(),
						user.getPhone(), user.getAddress(), user.getUserRole()));
	}

	// Đăng ký người dùng mới bằng email, username và password
	public AccountEntity registerUser(String email, String username, String password) {
		// Kiểm tra trùng lặp email
		if (accountRepository.findByEmail(email).isPresent()) {
			throw new RuntimeException("Email đã tồn tại!");
		}

		// Tạo và lưu người dùng mới
		AccountEntity user = new AccountEntity(email, username, password); // Thêm username vào constructor
		return accountRepository.save(user); // Không cần phải gọi setEmail, setUsername, setPassword nữa
	}

	// Lấy tất cả người dùng với thông tin tài khoản
	public List<AccountResponse> getAllUsersWithAccountInfo() {
		return accountRepository.getAllUsersWithAccountInfo(); // Trả về danh sách AccountResponse
	}

	public void deleteUserById(long userID) {
		Optional<AccountEntity> user = accountRepository.findById(userID);
		if (user.isPresent()) {
			// Sử dụng phương thức deleteByUserID trong repository
			accountRepository.deleteByUserID(userID);
		} else {
			throw new RuntimeException("User not found with ID: " + userID);
		}
	}

	// Cập nhật thông tin profile của người dùng
	public AccountEntity updateUserProfile(long userID, String name, String phone, String address) {
		Optional<AccountEntity> accountOpt = accountRepository.findById(userID);

		if (accountOpt.isPresent()) {
			AccountEntity account = accountOpt.get();
			account.setName(name);
			account.setPhone(phone);
			account.setAddress(address);
			account.setUserRole(userID);
			// Cập nhật thời gian cập nhật
			account.setUpdateAt(LocalDateTime.now());
			return accountRepository.save(account); // Lưu lại thông tin đã được cập nhật
		} else {
			throw new RuntimeException("User not found with ID: " + userID);
		}
	}

}

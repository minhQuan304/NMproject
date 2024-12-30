package com.example.NMproject.service;

import java.time.LocalDate;
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
//	public Optional<AccountResponse> loginUser(String email, String password) {
//		return accountRepository.findByEmailAndPassword(email, password)
//				.map(user -> new AccountResponse(user.getUserID(), user.getUsername(), user.getEmail(), user.getName(),
//						user.getPhone(), user.getAddress(), user.getUserRole()));
//	}
	public Optional<AccountResponse> loginUser(String email, String password) {
		Optional<AccountEntity> user = accountRepository.findByEmailAndPassword(email, password);

		if (user.isPresent()) {
			AccountEntity account = user.get();
			if (account.getStatus() == 1) {
				return Optional.of(new AccountResponse(account.getUserID(), account.getUsername(), account.getEmail(),
						account.getName(), account.getPhone(), account.getAddress(), account.getUserRole()));
			} else {
				// Trả về thông báo lỗi nếu status không phải là 1
				throw new RuntimeException("Account is not active (status is not 1).");
			}
		} else {
			throw new RuntimeException("Invalid email or password.");
		}
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

	public long deleteUserById(long userID) {
		// Tìm user theo ID
		Optional<AccountEntity> user = accountRepository.findById(userID);

		if (user.isPresent()) {
			// Cập nhật status của user về 0
			AccountEntity account = user.get();
			account.setStatus(0); // Giả sử bạn có một thuộc tính status trong AccountEntity
			accountRepository.save(account);

			// Trả về giá trị status sau khi cập nhật
			return account.getStatus();
		} else {
			throw new RuntimeException("User not found with ID: " + userID);
		}
	}

	public long activeUserById(long userID) {
		Optional<AccountEntity> user = accountRepository.findById(userID);
		if (user.isPresent()) {
			AccountEntity account = user.get();
			account.setStatus(1); // Chuyển status thành 1
			accountRepository.save(account); // Lưu thay đổi
			return account.getStatus(); // Trả về status mới
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
			account.setUpdateAt(LocalDate.now());
			return accountRepository.save(account); // Lưu lại thông tin đã được cập nhật
		} else {
			throw new RuntimeException("User not found with ID: " + userID);
		}
	}

}

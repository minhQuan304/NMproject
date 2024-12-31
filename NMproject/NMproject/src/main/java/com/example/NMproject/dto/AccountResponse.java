package com.example.NMproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponse {
	private long userID;
	private String username;
	private String email;
	private String name;
	private String phone;
	private String address;
	private String imageLink = "/hinh_anh/avatar.jpg"; // Đặt giá trị mặc định cho avatarUrl
	private long userRole; // Thêm userRole vào
	private long status;

	// Constructor không có avatarUrl và userRole
	public AccountResponse(long userID, String email, String username) {
		this.userID = userID;
		this.email = email;
		this.username = username;
	}

	// Constructor không có userRole
	public AccountResponse(long userID, String username, String email, String name, String phone, String address,
			long userRole) {
		this.userID = userID;
		this.username = username;
		this.email = email;
		this.name = name;
		this.phone = phone;
		this.address = address;
		this.userRole = userRole;
	}

	// Constructor có đầy đủ tất cả các trường
	public AccountResponse(long userID, String username, String email, String name, String phone, String address,
			long userRole, long status) {
		this.userID = userID;
		this.username = username;
		this.email = email;
		this.name = name;
		this.phone = phone;
		this.address = address;
		this.userRole = userRole;
		this.status = status;
	}
}

package com.example.NMproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponse {
	private Integer id;
	private String email;
	private String username;
	private String avatarUrl; // Thêm trường avatarUrl cho ảnh đại diện

	// Bạn có thể tạo một constructor cho trường hợp không cần avatarUrl
	public AccountResponse(Integer id, String email, String username) {
		this.id = id;
		this.email = email;
		this.username = username;
		this.avatarUrl = "/hinh_anh/hinh_anh.jpg"; // Đường dẫn ảnh mặc định
	}
}

package com.example.NMproject.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class BorrowBookDTO {
	private long userID; // ID người dùng
	private String username; // Tên người dùng
	private List<BorrowedBookDetailDTO> borrowedBooks;

	@Data
	public static class BorrowedBookDetailDTO {
		private Long borrowID; // Thay đổi thành Integeru
		private Long bookID;
		private String title;
		private LocalDate borrowDate;
		private LocalDate dueDate;
	}

	// Thêm thông tin để gửi dữ liệu khi thêm sách mượn
	private Long bookID; // Dành cho việc thêm sách mượn
	private String title; // Dành cho việc thêm sách mượn
	private LocalDate borrowDate; // Dành cho việc thêm sách mượn
	private LocalDate dueDate; // Dành cho việc thêm sách mượn
}

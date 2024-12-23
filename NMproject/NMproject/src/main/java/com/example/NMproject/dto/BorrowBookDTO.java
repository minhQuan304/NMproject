package com.example.NMproject.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class BorrowBookDTO {
	private long userId; // ID người dùng
	private String username; // Tên người dùng
	private List<BorrowedBookDetailDTO> borrowedBooks;

	@Data
	public static class BorrowedBookDetailDTO {
		private Long borrowId; // Thay đổi thành Integeru
		private Long bookId;
		private String title;
		private LocalDateTime borrowDate;
		private LocalDateTime dueDate;
	}

	// Thêm thông tin để gửi dữ liệu khi thêm sách mượn
	private Long bookId; // Dành cho việc thêm sách mượn
	private String title; // Dành cho việc thêm sách mượn
	private LocalDateTime borrowDate; // Dành cho việc thêm sách mượn
	private LocalDateTime dueDate; // Dành cho việc thêm sách mượn
}

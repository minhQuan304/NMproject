package com.example.NMproject.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.NMproject.dto.BorrowBookDTO;
import com.example.NMproject.service.BorrowBookService;

@RestController
@RequestMapping("/api/borrowed-books")
public class BorrowBookController {

	@Autowired
	private BorrowBookService borrowBookService;

	// API để lấy danh sách sách mượn của người dùng theo userId
	@GetMapping("/user/{userId}")
	public List<BorrowBookDTO.BorrowedBookDetailDTO> getBorrowDetailsByUserId(@PathVariable Long userId) {
		// Lấy danh sách các sách mượn của người dùng theo userId
		return borrowBookService.getBorrowDetailsByUserId(userId);
	}

	@DeleteMapping("/delete/{borrowID}")
	public ResponseEntity<?> deleteBorrowedBook(@PathVariable Long borrowID) {
		borrowBookService.deleteBorrowedBook(borrowID);
		return ResponseEntity.ok().body(Map.of("message", "Xóa sách mượn thành công với borrowID: " + borrowID));
	}

	// Ví dụ giả định controller của bạn
	@PostMapping("/addborrowBook")
	public ResponseEntity<BorrowBookDTO.BorrowedBookDetailDTO> borrowBook(@RequestBody BorrowBookDTO borrowBookDTO) {
		try {
			// Gọi service để thêm sách mượn và trả về DTO
			BorrowBookDTO.BorrowedBookDetailDTO borrowedBookDetailDTO = borrowBookService
					.addBorrowedBook(borrowBookDTO);
			return ResponseEntity.ok(borrowedBookDetailDTO);
		} catch (Exception e) {
			return ResponseEntity.status(400).body(null); // Trả về lỗi nếu xảy ra vấn đề
		}
	}
}

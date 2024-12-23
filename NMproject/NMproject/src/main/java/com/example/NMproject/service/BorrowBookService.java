package com.example.NMproject.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.NMproject.dto.BorrowBookDTO;
import com.example.NMproject.repository.BorrowBookRepository;

import jakarta.transaction.Transactional;

@Service
public class BorrowBookService {

	@Autowired
	private BorrowBookRepository borrowBookRepository;

	@Transactional
	public List<BorrowBookDTO.BorrowedBookDetailDTO> getBorrowDetailsByUserId(Long userId) {
		// Lấy danh sách các thông tin sách mượn của người dùng theo userId
		List<Object[]> borrowDetails = borrowBookRepository.findBorrowDetailsByUserId(userId);

		// Tạo danh sách sách mượn
		List<BorrowBookDTO.BorrowedBookDetailDTO> borrowedBooks = new ArrayList<>();
		for (Object[] detail : borrowDetails) {
			BorrowBookDTO.BorrowedBookDetailDTO borrowedBookDetailDTO = new BorrowBookDTO.BorrowedBookDetailDTO();
			borrowedBookDetailDTO.setBorrowId((Long) detail[0]); // borrowID
			borrowedBookDetailDTO.setBookId((Long) detail[2]); // bookID
			borrowedBookDetailDTO.setTitle((String) detail[3]); // Title
			borrowedBookDetailDTO.setBorrowDate((LocalDateTime) detail[4]); // Borrow date
			borrowedBookDetailDTO.setDueDate((LocalDateTime) detail[5]); // Due date
			borrowedBooks.add(borrowedBookDetailDTO);
		}
		return borrowedBooks;
	}

	@Transactional
	public void deleteBorrowedBook(Long borrowID) { // Đổi sang Long
		// Kiểm tra tồn tại borrowID
		if (!borrowBookRepository.existsById(borrowID)) {
			throw new IllegalArgumentException("Borrow ID không tồn tại: " + borrowID);
		}
		borrowBookRepository.deleteByBorrowID(borrowID);
	}

}

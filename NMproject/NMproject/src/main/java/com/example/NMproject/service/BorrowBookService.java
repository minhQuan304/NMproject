package com.example.NMproject.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.NMproject.dto.BorrowBookDTO;
import com.example.NMproject.entity.AccountEntity;
import com.example.NMproject.entity.Book;
import com.example.NMproject.entity.BorrowBook;
import com.example.NMproject.repository.AccountRepository;
import com.example.NMproject.repository.BookRepository;
import com.example.NMproject.repository.BorrowBookRepository;

import jakarta.transaction.Transactional;

@Service
public class BorrowBookService {

	@Autowired
	private BorrowBookRepository borrowBookRepository;
	@Autowired
	private AccountRepository accountRepository; // Inject AccountRepository

	@Autowired
	private BookRepository bookRepository; // Inject BookRepository

	@Transactional
	public List<BorrowBookDTO.BorrowedBookDetailDTO> getBorrowDetailsByUserId(Long userId) {
		// Lấy danh sách các thông tin sách mượn của người dùng theo userId
		List<Object[]> borrowDetails = borrowBookRepository.findBorrowDetailsByUserId(userId);

		// Tạo danh sách sách mượn
		List<BorrowBookDTO.BorrowedBookDetailDTO> borrowedBooks = new ArrayList<>();
		for (Object[] detail : borrowDetails) {
			BorrowBookDTO.BorrowedBookDetailDTO borrowedBookDetailDTO = new BorrowBookDTO.BorrowedBookDetailDTO();
			borrowedBookDetailDTO.setBorrowID((Long) detail[0]); // borrowID
			borrowedBookDetailDTO.setBookID((Long) detail[2]); // bookID
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

	@Transactional
	public BorrowBookDTO.BorrowedBookDetailDTO addBorrowedBook(BorrowBookDTO borrowBookDTO) {
		// Lấy AccountEntity và Book từ cơ sở dữ liệu
		AccountEntity account = accountRepository.findById(borrowBookDTO.getUserID()).orElseThrow(
				() -> new IllegalArgumentException("User not found with ID: " + borrowBookDTO.getUserID()));

		Book book = bookRepository.findById(borrowBookDTO.getBookID()).orElseThrow(
				() -> new IllegalArgumentException("Book not found with ID: " + borrowBookDTO.getBookID()));

		// Tạo đối tượng BorrowBook
		BorrowBook borrowBook = new BorrowBook();
		borrowBook.setAccount(account);
		borrowBook.setBook(book);
		borrowBook.setBorrowDate(borrowBookDTO.getBorrowDate());
		borrowBook.setDueDate(borrowBookDTO.getDueDate());

		// Lưu vào cơ sở dữ liệu
		BorrowBook savedBorrowBook = borrowBookRepository.save(borrowBook);

		// Tạo DTO để trả về
		BorrowBookDTO.BorrowedBookDetailDTO borrowedBookDetailDTO = new BorrowBookDTO.BorrowedBookDetailDTO();
		borrowedBookDetailDTO.setBorrowID(savedBorrowBook.getBorrowID());
		borrowedBookDetailDTO.setBookID(book.getBookId());
		borrowedBookDetailDTO.setTitle(book.getTitle());
		borrowedBookDetailDTO.setBorrowDate(savedBorrowBook.getBorrowDate());
		borrowedBookDetailDTO.setDueDate(savedBorrowBook.getDueDate());

		return borrowedBookDetailDTO;
	}

}

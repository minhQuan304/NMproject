package com.example.NMproject.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.NMproject.dto.BookDTO;
import com.example.NMproject.entity.Book;
import com.example.NMproject.service.BookService;

@Controller
@RequestMapping("api/books")
public class BookController {

	@Autowired
	private BookService bookService;

	@GetMapping("/bookss")
	public String bookManagement() {
		return "BookManagement"; // Tham chiếu đến BookManagement.html trong thư mục /templates
	}

	@GetMapping("/allbooks")
	public ResponseEntity<List<BookDTO>> getAllBooks() {
		try {
			List<Book> books = bookService.getAllBooks();
			List<BookDTO> bookDTOs = books.stream()
					.map(book -> BookDTO.builder().bookId(book.getBookId()).title(book.getTitle())
							.category(book.getCategory()).author(book.getAuthor()).publishDate(book.getPublishDate())
							.imageLink(book.getImageLink()).description(book.getDescription())
							.quantityTotal(book.getQuantityTotal()).quantityValid(book.getQuantityValid())
							.rate(book.getRate()).build())
					.collect(Collectors.toList());
			return ResponseEntity.ok(bookDTOs);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	// Endpoint tìm kiếm sách theo từ khóa
	@PostMapping("/search")
	public ResponseEntity<List<BookDTO>> searchBooksByKeyword(@RequestBody Map<String, String> payload) {
		try {
			String keyword = payload.get("keyword");
			if (keyword == null || keyword.trim().isEmpty()) {
				return ResponseEntity.badRequest().body(Collections.emptyList());
			}
			List<BookDTO> books = bookService.searchBooksByKeyword(keyword).stream()
					.map(book -> BookDTO.builder().bookId(book.getBookId()).title(book.getTitle())
							.category(book.getCategory()).author(book.getAuthor()).publishDate(book.getPublishDate())
							.imageLink(book.getImageLink()).description(book.getDescription())
							.quantityTotal(book.getQuantityTotal()).quantityValid(book.getQuantityValid())
							.rate(book.getRate()).build())
					.collect(Collectors.toList());
			return ResponseEntity.ok(books);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList()); // Trả về danh
																											// sách rỗng
																											// nếu có
																											// lỗi
		}
	}

	@PostMapping("/addbooks")
	public ResponseEntity<String> addBook(@RequestBody Book book) {
		try {
			if (book == null || book.getTitle() == null || book.getTitle().isEmpty()) {
				return ResponseEntity.badRequest().body("Book information is incomplete or invalid");
			}

			bookService.addBook(book);

			return ResponseEntity.status(HttpStatus.CREATED).body("Book added successfully");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding book");
		}
	}

	@PostMapping("/update")
	public ResponseEntity<String> updateBook(@RequestBody Book book) {
		try {
			// Kiểm tra nếu đối tượng sách hợp lệ
			if (book == null || book.getBookId() == null) {
				return ResponseEntity.badRequest().body("Book ID is required to update the book");
			}

			// Gọi service để sửa thông tin sách
			bookService.updateBook(book);

			// Trả về thông báo thành công
			return ResponseEntity.status(HttpStatus.OK).body("Book updated successfully");
		} catch (Exception e) {
			// Trả về lỗi nếu có ngoại lệ
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating book");
		}
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteBook(@PathVariable Long id) {
		try {
			bookService.deleteBookById(id);
			return ResponseEntity.status(HttpStatus.OK).body("Book deleted successfully");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting book");
		}
	}

}

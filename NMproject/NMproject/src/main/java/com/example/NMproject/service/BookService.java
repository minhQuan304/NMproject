package com.example.NMproject.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.NMproject.entity.Book;
import com.example.NMproject.repository.BookRepository;

@Service
public class BookService {

	@Autowired
	private BookRepository bookRepository;

	public List<Book> getAllBooks() {
		return bookRepository.findAll();
	}

	// Phương thức tìm kiếm sách theo từ khóa
	public List<Book> searchBooksByKeyword(String keyword) {
		return bookRepository.searchBooksByKeyword(keyword);
	}

	// Phương thức thêm sách vào cơ sở dữ liệu
	public Book addBook(Book book) {
		try {
			Book savedBook = bookRepository.save(book);
			System.out.println("Book added successfully: " + savedBook);
			return savedBook;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error adding book: " + e.getMessage());
		}
	}

	public Book updateBook(Book updatedBook) {
		// Kiểm tra xem sách có tồn tại trong cơ sở dữ liệu không
		Book existingBook = bookRepository.findById(updatedBook.getBookId()).orElse(null);
		if (existingBook == null) {
			throw new RuntimeException("Book not found with ID: " + updatedBook.getBookId());
		}

		// Cập nhật thông tin sách
		existingBook.setTitle(updatedBook.getTitle());
		existingBook.setCategory(updatedBook.getCategory());
		existingBook.setAuthor(updatedBook.getAuthor());
		existingBook.setPublishDate(updatedBook.getPublishDate());
		existingBook.setImageLink(updatedBook.getImageLink());
		existingBook.setDescription(updatedBook.getDescription());
		existingBook.setQuantityTotal(updatedBook.getQuantityTotal());
		existingBook.setQuantityValid(updatedBook.getQuantityValid());
		existingBook.setRate(updatedBook.getRate());

		// Lưu sách đã được cập nhật lại vào cơ sở dữ liệu
		return bookRepository.save(existingBook);
	}

	public void deleteBookById(Long bookId) {
		if (!bookRepository.existsById(bookId)) {
			throw new RuntimeException("Book not found with ID: " + bookId);
		}
		bookRepository.deleteById(bookId);
	}

}

package com.example.NMproject.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.NMproject.dto.BookDTO;
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
	public void addBook(BookDTO bookDTO) {
		// Chuyển đổi từ BookDTO thành Book (nếu cần)
		Book book = new Book();
		book.setTitle(bookDTO.getTitle());
		book.setCategory(bookDTO.getCategory());
		book.setAuthor(bookDTO.getAuthor());
		book.setPublishDate(bookDTO.getPublishDate());
		book.setImageLink(bookDTO.getImageLink());
		book.setDescription(bookDTO.getDescription());
		book.setQuantityTotal(bookDTO.getQuantityTotal());
		book.setQuantityValid(bookDTO.getQuantityValid());
		book.setRate(bookDTO.getRate());

		// Lưu sách vào cơ sở dữ liệu
		bookRepository.save(book);
	}

	public BookDTO updateBook(Long id, BookDTO bookDTO) {
		// Kiểm tra xem sách có tồn tại trong cơ sở dữ liệu không
		Book existingBook = bookRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Book not found with ID: " + id));

		// Cập nhật thông tin sách
		existingBook.setTitle(bookDTO.getTitle());
		existingBook.setCategory(bookDTO.getCategory());
		existingBook.setAuthor(bookDTO.getAuthor());
		existingBook.setPublishDate(bookDTO.getPublishDate());
		existingBook.setImageLink(bookDTO.getImageLink());
		existingBook.setDescription(bookDTO.getDescription());
		existingBook.setQuantityTotal(bookDTO.getQuantityTotal());
		existingBook.setQuantityValid(bookDTO.getQuantityValid());
		existingBook.setRate(bookDTO.getRate());

		// Lưu lại sách đã cập nhật vào cơ sở dữ liệu
		Book updatedBook = bookRepository.save(existingBook);

		// Chuyển đổi đối tượng Book thành BookDTO để trả về
		return BookDTO.builder().bookId(updatedBook.getBookId()).title(updatedBook.getTitle())
				.category(updatedBook.getCategory()).author(updatedBook.getAuthor())
				.publishDate(updatedBook.getPublishDate()).imageLink(updatedBook.getImageLink())
				.description(updatedBook.getDescription()).quantityTotal(updatedBook.getQuantityTotal())
				.quantityValid(updatedBook.getQuantityValid()).rate(updatedBook.getRate()).build();
	}

	public void deleteBookById(Long id) {
		// Kiểm tra xem sách có tồn tại không
		Book existingBook = bookRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Book not found with ID: " + id));

		// Xóa sách
		bookRepository.delete(existingBook);
	}

	public Book getBookById(Long id) {
		return bookRepository.findById(id).orElse(null); // Trả về sách nếu tìm thấy, nếu không trả về null
	}

}

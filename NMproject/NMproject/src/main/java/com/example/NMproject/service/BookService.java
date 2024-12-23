package com.example.NMproject.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.NMproject.dto.BookDTO;
import com.example.NMproject.entity.Book;
import com.example.NMproject.repository.BookRepository;

@Service
public class BookService {

	@Autowired
	private BookRepository bookRepository;

	public List<BookDTO> getAllBooks() {
		return bookRepository.findAllBooks().stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	public List<BookDTO> searchBooksByKeyword(String keyword) {
		return bookRepository.searchBooksByKeyword(keyword).stream().map(this::convertToDTO)
				.collect(Collectors.toList());
	}

	public BookDTO getBookById(Long id) {
		Book book = bookRepository.findBookById(id)
				.orElseThrow(() -> new RuntimeException("Book not found with ID: " + id));
		return convertToDTO(book);
	}

	public BookDTO addBook(BookDTO bookDTO) {
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

		bookRepository.save(book);
		return convertToDTO(book);
	}

	public BookDTO updateBook(Long id, BookDTO bookDTO) {
		Book existingBook = bookRepository.findBookById(id)
				.orElseThrow(() -> new RuntimeException("Book not found with ID: " + id));

		existingBook.setTitle(bookDTO.getTitle());
		existingBook.setCategory(bookDTO.getCategory());
		existingBook.setAuthor(bookDTO.getAuthor());
		existingBook.setPublishDate(bookDTO.getPublishDate());
		existingBook.setImageLink(bookDTO.getImageLink());
		existingBook.setDescription(bookDTO.getDescription());
		existingBook.setQuantityTotal(bookDTO.getQuantityTotal());
		existingBook.setQuantityValid(bookDTO.getQuantityValid());
		existingBook.setRate(bookDTO.getRate());

		bookRepository.save(existingBook);
		return convertToDTO(existingBook);
	}

	public void deleteBookById(Long id) {
		bookRepository.deleteBookById(id);
	}

	private BookDTO convertToDTO(Book book) {
		return BookDTO.builder().bookID(book.getBookId()).title(book.getTitle()).category(book.getCategory())
				.author(book.getAuthor()).publishDate(book.getPublishDate()).imageLink(book.getImageLink())
				.description(book.getDescription()).quantityTotal(book.getQuantityTotal())
				.quantityValid(book.getQuantityValid()).rate(book.getRate()).build();
	}
}

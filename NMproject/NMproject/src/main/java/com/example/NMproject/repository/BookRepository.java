package com.example.NMproject.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.NMproject.entity.Book;

import jakarta.transaction.Transactional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

	@Query("SELECT b FROM Book b")
	List<Book> findAllBooks();

	@Query("SELECT b FROM Book b WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%')) "
			+ "OR LOWER(b.author) LIKE LOWER(CONCAT('%', :keyword, '%')) "
			+ "OR LOWER(b.category) LIKE LOWER(CONCAT('%', :keyword, '%')) "
			+ "OR LOWER(b.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
	List<Book> searchBooksByKeyword(String keyword);

	@Query("SELECT b FROM Book b WHERE b.bookId = :id")
	Optional<Book> findBookById(Long id);

	@Modifying
	@Transactional
	@Query("DELETE FROM Book b WHERE b.bookId = :id")
	void deleteBookById(Long id);

}

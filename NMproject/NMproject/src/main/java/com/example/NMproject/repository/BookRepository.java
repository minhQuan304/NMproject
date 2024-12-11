package com.example.NMproject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.NMproject.entity.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

	// Ví dụ, truy vấn để tìm sách theo từ khóa
	@Query("SELECT b FROM Book b WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%')) "
			+ "OR LOWER(b.author) LIKE LOWER(CONCAT('%', :keyword, '%')) "
			+ "OR LOWER(b.category) LIKE LOWER(CONCAT('%', :keyword, '%')) "
			+ "OR LOWER(b.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
	List<Book> searchBooksByKeyword(String keyword);

}

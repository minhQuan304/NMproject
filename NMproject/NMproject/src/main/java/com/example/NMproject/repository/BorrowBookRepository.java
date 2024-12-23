package com.example.NMproject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.NMproject.entity.BorrowBook;

@Repository
public interface BorrowBookRepository extends JpaRepository<BorrowBook, Long> {

	// Truy vấn để lấy borrowDate, username và title theo userId, sắp xếp theo
	// borrowId
	@Query("SELECT b.borrowID, b.account.username, b.book.bookId, b.book.title, b.borrowDate, b.dueDate FROM BorrowBook b WHERE b.account.userID = :userId ORDER BY b.borrowID ASC")
	List<Object[]> findBorrowDetailsByUserId(@Param("userId") Long userId);

	@Modifying
	@Query("DELETE FROM BorrowBook b WHERE b.borrowID = :borrowID")
	void deleteByBorrowID(@Param("borrowID") Long borrowID);

}

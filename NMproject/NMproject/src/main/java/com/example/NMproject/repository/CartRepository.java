package com.example.NMproject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.NMproject.entity.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> { // Thay CartEntity thành Cart

	// Sử dụng @Query để lấy thông tin sách theo userID

	@Query("SELECT b.bookId, b.title, b.category, b.imageLink "
			+ "FROM Cart c JOIN c.book b WHERE c.user.userID = :userID")
	List<Object[]> findBooksByUserId(Long userID);
}

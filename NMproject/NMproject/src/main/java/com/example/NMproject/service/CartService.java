package com.example.NMproject.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.NMproject.repository.CartRepository;

@Service
public class CartService {

	@Autowired
	private CartRepository cartRepository;

	public List<Map<String, Object>> getBooksByUserId(Long userID) {
		// Gọi phương thức repository và lấy kết quả là danh sách Object[]
		List<Object[]> results = cartRepository.findBooksByUserId(userID);

		// Tạo danh sách Map để chứa thông tin sách
		List<Map<String, Object>> books = new ArrayList<>();

		// Duyệt qua kết quả và ánh xạ thành Map
		for (Object[] result : results) {
			Map<String, Object> book = new HashMap<>();
			book.put("bookId", result[0]);
			book.put("title", result[1]);
			book.put("category", result[2]);
			book.put("imageLink", result[3]);
			books.add(book);
		}

		return books;
	}
}

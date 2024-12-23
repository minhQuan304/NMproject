package com.example.NMproject.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.NMproject.service.CartService;

@RestController
@RequestMapping("/api/cart")
public class CartController {

	@Autowired
	private CartService cartService;

	@GetMapping("/books/{userID}")
	public List<Map<String, Object>> getBooksByUserId(@PathVariable Long userID) {
		// Gọi Service để lấy danh sách các sách
		return cartService.getBooksByUserId(userID);
	}
}

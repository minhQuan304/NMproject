package com.example.NMproject.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.NMproject.dto.AddCartResponse;
import com.example.NMproject.dto.AddItemRequest;
import com.example.NMproject.dto.CartResponse;
import com.example.NMproject.dto.DeleteItemRequest;
import com.example.NMproject.service.CartService;

@RestController
@RequestMapping("/api/cart")
public class CartController {

	@Autowired
	private CartService cartService;

	// API lấy tất cả sách trong giỏ của người dùng
	@GetMapping("/getAll/{userID}")
	public List<CartResponse> getAllBooks(@PathVariable Long userID) {
		return cartService.getAllBooksByUserId(userID);
	}

	// API thêm sách vào giỏ
//	@PutMapping("/addItem")
//	public ResponseEntity<String> addItem(@RequestBody AddItemRequest addItemRequest) {
//		String message = cartService.addBookToCart(addItemRequest.getUserID(), addItemRequest.getBookID());
//		return ResponseEntity.ok(message);
//	}
	@PutMapping("/addItem")
	public ResponseEntity<AddCartResponse> addItem(@RequestBody AddItemRequest addItemRequest) {
		String message = cartService.addBookToCart(addItemRequest.getUserID(), addItemRequest.getBookID());
		AddCartResponse response = new AddCartResponse(message);
		return ResponseEntity.ok(response);
	}

	// API xóa sách khỏi giỏ
	@DeleteMapping("/deleteItem")
	public ResponseEntity<String> deleteItem(@RequestBody DeleteItemRequest request) {
		try {
			// Gọi service để xóa sách khỏi giỏ hàng
			cartService.deleteBooksFromCart(request.getUserID(), request.getBookID());

			// Trả về thông báo xóa thành công
			return ResponseEntity.ok("Xóa thành công");
		} catch (Exception e) {
			e.printStackTrace(); // In ra lỗi để kiểm tra
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Có lỗi xảy ra khi xóa sách.");
		}
	}
}
//package com.example.NMproject.controller;
//
//import java.util.List;
//import java.util.Map;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.example.NMproject.service.CartService;
//
//@RestController
//@RequestMapping("/api/cart")
//public class CartController {
//
//	@Autowired
//	private CartService cartService;
//
//	@GetMapping("/books/{userID}")
//	public List<Map<String, Object>> getBooksByUserId(@PathVariable Long userID) {
//		// Gọi Service để lấy danh sách các sách
//		return cartService.getBooksByUserId(userID);
//	}
//}

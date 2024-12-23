package com.example.NMproject.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.NMproject.dto.CartResponse;
import com.example.NMproject.entity.Cart;
import com.example.NMproject.repository.BookRepository;
import com.example.NMproject.repository.CartRepository;

import jakarta.transaction.Transactional;

@Service
public class CartService {

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private BookRepository bookRepository;

	// Lấy tất cả các sách trong giỏ hàng của người dùng
	public List<CartResponse> getAllBooksByUserId(Long userID) {
		// Lấy Cart của người dùng
		Cart cart = cartRepository.findByUserID_UserID(userID);

		if (cart == null || cart.getBookIDs() == null || cart.getBookIDs().length == 0) {
			return List.of(); // Trả về danh sách trống
		}

		// Lấy thông tin chi tiết của từng cuốn sách trong giỏ hàng
		return Arrays.stream(cart.getBookIDs()).map(bookID -> bookRepository.findBookById(bookID).orElse(null))
				.filter(book -> book != null).map(book -> new CartResponse(book.getBookId(), book.getImageLink(),
						book.getCategory(), book.getTitle()))
				.collect(Collectors.toList());
	}

	// Thêm sách vào giỏ hàng
	public String addBookToCart(Long userID, Long bookID) {
		// Tìm Cart của người dùng
		Cart cart = cartRepository.findByUserID_UserID(userID);

		if (cart == null) {
			return "Giỏ hàng không tồn tại!";
		}

		Long[] currentBookIDs = cart.getBookIDs();

		// Kiểm tra nếu bookID đã có trong giỏ hàng thì không thêm
		if (currentBookIDs != null && Arrays.asList(currentBookIDs).contains(bookID)) {
			return "Sách đã có trong giỏ!";
		}

		// Nếu giỏ hàng không có sách, tạo mảng mới
		if (currentBookIDs == null) {
			cart.setBookIDs(new Long[] { bookID });
		} else {
			// Thêm sách vào mảng
			Long[] newBookIDs = Arrays.copyOf(currentBookIDs, currentBookIDs.length + 1);
			newBookIDs[currentBookIDs.length] = bookID;
			cart.setBookIDs(newBookIDs);
		}

		cartRepository.save(cart); // Lưu giỏ hàng cập nhật
		return "Thêm thành công!";
	}

	// Xóa sách khỏi giỏ hàng
	@Transactional
	public void deleteBooksFromCart(Long userID, Long[] bookIDs) {
		// Tìm giỏ hàng của người dùng theo userID
		Cart cart = cartRepository.findByUserID_UserID(userID);

		if (cart == null || cart.getBookIDs() == null || cart.getBookIDs().length == 0) {
			throw new RuntimeException("Giỏ hàng không tồn tại hoặc không có sách.");
		}

		// Kiểm tra xem mảng bookIDs có hợp lệ không
		if (bookIDs == null || bookIDs.length == 0) {
			throw new RuntimeException("Không có sách nào để xóa.");
		}

		// Lấy mảng bookIDs hiện tại trong giỏ hàng
		Long[] currentBookIDs = cart.getBookIDs();
		List<Long> currentBookList = new ArrayList<>(Arrays.asList(currentBookIDs));

		// Xóa các bookID trong currentBookList mà có mặt trong bookIDs
		currentBookList.removeAll(Arrays.asList(bookIDs));

		// Nếu không có gì để xóa, trả về thông báo thích hợp
		if (currentBookList.size() == currentBookIDs.length) {
			throw new RuntimeException("Không có sách nào để xóa.");
		}

		// Cập nhật lại giỏ hàng với mảng mới
		cart.setBookIDs(currentBookList.toArray(new Long[0]));
		cartRepository.save(cart); // Lưu lại Cart đã được cập nhật
	}
}
//package com.example.NMproject.service;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.example.NMproject.repository.CartRepository;
//
//@Service
//public class CartService {
//
//	@Autowired
//	private CartRepository cartRepository;
//
//	public List<Map<String, Object>> getBooksByUserId(Long userID) {
//		// Gọi phương thức repository và lấy kết quả là danh sách Object[]
//		List<Object[]> results = cartRepository.findBooksByUserId(userID);
//
//		// Tạo danh sách Map để chứa thông tin sách
//		List<Map<String, Object>> books = new ArrayList<>();
//
//		// Duyệt qua kết quả và ánh xạ thành Map
//		for (Object[] result : results) {
//			Map<String, Object> book = new HashMap<>();
//			book.put("bookId", result[0]);
//			book.put("title", result[1]);
//			book.put("category", result[2]);
//			book.put("imageLink", result[3]);
//			books.add(book);
//		}
//
//		return books;
//	}
//}

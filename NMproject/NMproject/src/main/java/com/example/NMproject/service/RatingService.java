package com.example.NMproject.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.NMproject.dto.RatingDTO;
import com.example.NMproject.dto.RatingResponseDTO;
import com.example.NMproject.entity.Book;
import com.example.NMproject.entity.Rating;
import com.example.NMproject.repository.BookRepository;
import com.example.NMproject.repository.RatingRepository;

import jakarta.transaction.Transactional;

@Service
public class RatingService {

	@Autowired
	private RatingRepository ratingRepository;

	@Autowired
	private BookRepository bookRepository;

	// 1. Lấy danh sách đánh giá của người dùng
	public List<Rating> getRatesByUser(Long userID) {
		return ratingRepository.findByUserID(userID);
	}

	// 2. Cập nhật hoặc tạo mới đánh giá và tính lại rateAverage
	@Transactional
	public RatingResponseDTO createOrUpdateRating(RatingDTO ratingDTO) {
		// Kiểm tra nếu Rating đã tồn tại
		Optional<Rating> existingRating = ratingRepository.findByUserIDAndBookID(ratingDTO.getUserID(),
				ratingDTO.getBookID());

		Rating rating;
		if (existingRating.isPresent()) {
			// Nếu có thì cập nhật lại
			rating = existingRating.get();
			rating.setRate(ratingDTO.getRate());
		} else {
			// Nếu không thì tạo mới
			rating = new Rating(ratingDTO.getUserID(), ratingDTO.getBookID(), ratingDTO.getRate());
		}

		// Lưu đánh giá
		ratingRepository.save(rating);

		// Tính lại rateAverage cho Book và cập nhật vào bảng Book
		double rateAverage = calculateRateAverage(ratingDTO.getBookID());
		BigDecimal roundedRateAverage = new BigDecimal(rateAverage).setScale(1, RoundingMode.HALF_UP);
		updateBookRateAverage(ratingDTO.getBookID(), roundedRateAverage.doubleValue());

		// Trả về response
		return new RatingResponseDTO(roundedRateAverage.doubleValue(), rating.getRate(),
				"Đánh giá cuốn sách này thành công!");
	}

	// 3. Tính toán rateAverage của book
	private double calculateRateAverage(Long bookID) {
		List<Rating> ratings = ratingRepository.findByBookID(bookID);

		if (ratings.isEmpty()) {
			return 0.0; // Trường hợp không có đánh giá nào
		}

		double totalRate = ratings.stream().mapToDouble(Rating::getRate).sum();
		return totalRate / ratings.size();
	}

	// 4. Cập nhật rateAverage vào bảng Book
	private void updateBookRateAverage(Long bookID, double rateAverage) {
		Optional<Book> bookOptional = bookRepository.findById(bookID);

		if (bookOptional.isPresent()) {
			Book book = bookOptional.get();
			book.setRate(rateAverage);
			bookRepository.save(book); // Lưu lại cập nhật
		}
	}
}
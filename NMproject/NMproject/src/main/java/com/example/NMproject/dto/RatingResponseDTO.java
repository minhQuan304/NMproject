package com.example.NMproject.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RatingResponseDTO {
	private double rate; // Để lưu rate từ bảng Book
	private String message;

	// Constructor
	public RatingResponseDTO(double rate, String message) {
		this.rate = rate;
		this.message = "Đánh giá cuốn sách này thành công!";
	}
}

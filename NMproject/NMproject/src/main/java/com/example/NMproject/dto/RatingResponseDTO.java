package com.example.NMproject.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RatingResponseDTO {
	private double rateAverage;
	private double rate;
	private String message;

	// Constructor
	public RatingResponseDTO(double rateAverage, double rate, String message) {
		this.rateAverage = rateAverage;
		this.rate = rate;
		this.message = "Đánh giá cuốn sách này thành công!";
	}
}
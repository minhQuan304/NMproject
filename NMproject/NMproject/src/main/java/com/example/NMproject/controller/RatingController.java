package com.example.NMproject.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.NMproject.dto.RatingDTO;
import com.example.NMproject.dto.RatingResponseDTO;
import com.example.NMproject.entity.Rating;
import com.example.NMproject.service.RatingService;

@RestController
@RequestMapping("/api/rates")
public class RatingController {

	@Autowired
	private RatingService ratingService;

	@GetMapping("/getrate/{userID}")
	public ResponseEntity<List<Rating>> getRatesByUser(@PathVariable Long userID) {
		List<Rating> ratings = ratingService.getRatesByUser(userID);
		if (ratings.isEmpty()) {
			return ResponseEntity.noContent().build(); // Nếu không có đánh giá nào
		}
		return ResponseEntity.ok(ratings);
	}

	@PostMapping("/create")
	public ResponseEntity<RatingResponseDTO> createOrUpdateRating(@RequestBody RatingDTO ratingDTO) {
		RatingResponseDTO response = ratingService.createOrUpdateRating(ratingDTO);
		return ResponseEntity.ok(response);
	}
}
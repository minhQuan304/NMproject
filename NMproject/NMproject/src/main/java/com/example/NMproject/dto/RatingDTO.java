package com.example.NMproject.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RatingDTO {
	private Long userID;
	private Long bookID;
	private double rate;
}
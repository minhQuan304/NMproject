package com.example.NMproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse {
	private String message;
	private Object data;

	public ApiResponse(String message) {
		this.message = message;
	}
}

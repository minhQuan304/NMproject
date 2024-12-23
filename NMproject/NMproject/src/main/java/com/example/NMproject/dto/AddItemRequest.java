package com.example.NMproject.dto;

public class AddItemRequest {
	private Long userID;
	private Long bookID;

	// Getters and Setters

	public Long getUserID() {
		return userID;
	}

	public void setUserID(Long userID) {
		this.userID = userID;
	}

	public Long getBookID() {
		return bookID;
	}

	public void setBookID(Long bookID) {
		this.bookID = bookID;
	}
}
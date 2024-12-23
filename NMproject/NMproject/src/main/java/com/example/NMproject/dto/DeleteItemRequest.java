package com.example.NMproject.dto;

public class DeleteItemRequest {
	private Long userID;
	private Long[] bookID; // Mảng bookIDs

	// Getters và Setters
	public Long getUserID() {
		return userID;
	}

	public void setUserID(Long userID) {
		this.userID = userID;
	}

	public Long[] getBookID() {
		return bookID;
	}

	public void setBookID(Long[] bookIDs) {
		this.bookID = bookIDs;
	}
}
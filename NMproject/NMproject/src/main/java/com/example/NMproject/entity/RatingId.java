package com.example.NMproject.entity;

import java.io.Serializable;
import java.util.Objects;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RatingId implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long userID; // Sử dụng tên trường giống trong entity
	private Long bookID; // Sử dụng tên trường giống trong entity

	// Constructors
	public RatingId() {
	}

	public RatingId(Long userID, Long bookID) {
		this.userID = userID;
		this.bookID = bookID;
	}

	// equals() and hashCode()
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		RatingId ratingId = (RatingId) o;
		return Objects.equals(userID, ratingId.userID) && Objects.equals(bookID, ratingId.bookID);
	}

	@Override
	public int hashCode() {
		return Objects.hash(userID, bookID);
	}
}
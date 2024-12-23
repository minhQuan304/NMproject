package com.example.NMproject.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@IdClass(RatingId.class) // Liên kết với RatingId làm khóa chính composite
public class Rating {

	@Id
	@Column(name = "userid", nullable = false) // Cột trong cơ sở dữ liệu là 'userid'
	private Long userID; // Trường trong entity là 'userID'

	@Id
	@Column(name = "bookid", nullable = false) // Cột trong cơ sở dữ liệu là 'bookid'
	private Long bookID; // Trường trong entity là 'bookID'

	@Column(name = "rate", nullable = false)
	private Double rate;

	// Constructors
	public Rating() {
	}

	public Rating(Long userID, Long bookID, Double rate) {
		this.userID = userID;
		this.bookID = bookID;
		this.rate = rate;
	}
}
package com.example.NMproject.entity;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "borrowBook")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BorrowBook {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long borrowID;

	@ManyToOne
	@JoinColumn(name = "userID", referencedColumnName = "userID")
	@JsonIgnore
	private AccountEntity account;

	@ManyToOne
	@JoinColumn(name = "bookID", referencedColumnName = "bookId")
	@JsonIgnore
	private Book book;

	@Column(name = "borrowDate")
	private LocalDate borrowDate; // Thay đổi từ LocalDateTime thành LocalDate

	@Column(name = "dueDate")
	private LocalDate dueDate; // Thay đổi từ LocalDateTime thành LocalDate

	// Getter for username từ AccountEntity
	public String getUsername() {
		return account != null ? account.getUsername() : null;
	}

	// Getter for title từ Book
	public String getBookTitle() {
		return book != null ? book.getTitle() : null;
	}
}

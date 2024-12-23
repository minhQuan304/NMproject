package com.example.NMproject.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookDTO {

	private Long bookId;
	private String title;
	private String category;
	private String author;

	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date publishDate;

	private String imageLink;
	private String description;

	private Integer quantityTotal; // Tổng số sách
	private Integer quantityValid; // Số sách còn hợp lệ
	private Double rate; // Đánh giá quyển sách

	public BookDTO(Long bookId, String imageLink, String category, String title) {
		this.bookId = bookId;
		this.imageLink = imageLink;
		this.category = category;
		this.title = title;
	}
}

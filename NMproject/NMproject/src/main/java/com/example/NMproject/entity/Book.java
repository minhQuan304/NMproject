package com.example.NMproject.entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "book") // Tùy chọn, nếu bạn muốn chỉ định tên bảng trong DB
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long bookId;

	private String title;
	private String category;
	private String author;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date publishDate;

	private String imageLink;
	private String description;

	private Integer quantityTotal;
	private Integer quantityValid;
	private Double rate;
}

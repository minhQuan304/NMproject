package com.example.NMproject.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDTO {
	private Long bookID;
	private String comment;
	private Long commentID;
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	private LocalDateTime lastUpdate;
	private String pathPicture;
	private int userID;
	private String username;
}
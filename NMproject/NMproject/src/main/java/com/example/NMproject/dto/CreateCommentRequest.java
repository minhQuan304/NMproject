package com.example.NMproject.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCommentRequest {
	private Long bookID;
	private int userID;
	private String comment;
	private String contentEdited;
}
package com.example.NMproject.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.NMproject.dto.CommentDTO;
import com.example.NMproject.dto.CreateCommentRequest;
import com.example.NMproject.entity.AccountEntity;
import com.example.NMproject.entity.Comment;
import com.example.NMproject.repository.AccountRepository;
import com.example.NMproject.repository.CommentRepository;

@Service
public class CommentService {
	private final CommentRepository commentRepository;
	private final AccountRepository userRepository;

	public CommentService(CommentRepository commentRepository, AccountRepository userRepository) {
		this.commentRepository = commentRepository;
		this.userRepository = userRepository;
	}

	public List<CommentDTO> getCommentsByBookID(Long bookID) {
		List<Comment> comments = commentRepository.findByBookID(bookID);
		List<CommentDTO> commentDTOs = new ArrayList<>();

		for (Comment comment : comments) {
			AccountEntity user = userRepository.findById(comment.getUserID())
					.orElseThrow(() -> new RuntimeException("User not found"));

			CommentDTO dto = new CommentDTO();
			dto.setCommentID(comment.getCommentID()); // Đổi thành commentID mới
			dto.setUsername(user.getUsername()); // Đổi thành username mới
			dto.setPathPicture(user.getPathPicture()); // Đổi thành pathPicture mới
			dto.setComment(comment.getContent()); // Đổi từ content thành comment
			dto.setLastUpdate(comment.getLastUpdate()); // Đổi từ updateAt thành lastUpdate
			dto.setBookID(bookID); // Đặt bookID vào CommentDTO
			dto.setUserID(comment.getUserID()); // Đặt userID vào CommentDTO
			commentDTOs.add(dto);
		}

		return commentDTOs;
	}

	public Comment createComment(CreateCommentRequest request) {
		Comment comment = new Comment();
		comment.setBookID(request.getBookID());
		comment.setUserID(request.getUserID());
		comment.setContent(request.getComment());
		comment.setLastUpdate(LocalDateTime.now());
		return commentRepository.save(comment);
	}

	public void deleteComment(Long commentID) {
		commentRepository.deleteById(commentID);
	}

	public Comment updateComment(Long commentID, CreateCommentRequest request) {
		// Tìm bình luận theo commentID
		Comment comment = commentRepository.findById(commentID)
				.orElseThrow(() -> new RuntimeException("Comment not found"));

		// Cập nhật nội dung bình luận với contentEdited
		if (request.getContentEdited() != null && !request.getContentEdited().isEmpty()) {
			comment.setContent(request.getContentEdited());
		}

		// Cập nhật thời gian sửa (lastUpdate)
		comment.setLastUpdate(LocalDateTime.now());

		// Lưu bình luận đã cập nhật
		return commentRepository.save(comment);
	}
}
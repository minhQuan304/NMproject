package com.example.NMproject.controller;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.NMproject.dto.CommentDTO;
import com.example.NMproject.dto.CreateCommentRequest;
import com.example.NMproject.entity.Comment;
import com.example.NMproject.service.CommentService;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
	private final CommentService commentService;

	public CommentController(CommentService commentService) {
		this.commentService = commentService;
	}

	@GetMapping("/getcmt/{bookID}")
	public ResponseEntity<List<CommentDTO>> getComments(@PathVariable Long bookID) {
		List<CommentDTO> comments = commentService.getCommentsByBookID(bookID);
		if (comments.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
		return ResponseEntity.ok(comments);
	}

	@PostMapping("/post")
	public ResponseEntity<Map<String, Object>> postComment(@RequestBody CreateCommentRequest request) {
		Comment comment = commentService.createComment(request);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

		Map<String, Object> response = new HashMap<>();
		response.put("lastUpdate", comment.getLastUpdate().format(formatter));
		response.put("commentID", comment.getCommentID());
		response.put("message", "Bình luận đã được thêm thành công!");

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

//	@PutMapping("/{commentID}")
//	public ResponseEntity<Map<String, Object>> updateComment(@PathVariable Long commentID,
//			@RequestBody CreateCommentRequest request) {
//		if (request.getComment().length() < 3 || request.getComment().length() > 100) {
//			return ResponseEntity.badRequest().build();
//		}
//		Comment updatedComment = commentService.updateComment(commentID, request);
//
//		Map<String, Object> response = new HashMap<>();
//		response.put("message", "Bình luận đã được sửa thành công");
//		response.put("lastUpdate", updatedComment.getLastUpdate().toString());
//
//		return ResponseEntity.status(HttpStatus.OK).body(response);
//	}
	@PutMapping("/{commentID}")
	public ResponseEntity<Map<String, Object>> updateComment(@PathVariable Long commentID,
			@RequestBody CreateCommentRequest request) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

		// Cập nhật bình luận
		Comment updatedComment = commentService.updateComment(commentID, request);

		// Chuẩn bị phản hồi
		Map<String, Object> response = new HashMap<>();
		response.put("lastUpdate", updatedComment.getLastUpdate().format(formatter)); // Trả về lastUpdate mới

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@DeleteMapping("/{commentID}")
	public ResponseEntity<Void> deleteComment(@PathVariable Long commentID) {
		commentService.deleteComment(commentID);
		return ResponseEntity.noContent().build();
	}
}
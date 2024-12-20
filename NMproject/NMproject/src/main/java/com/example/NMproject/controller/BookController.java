package com.example.NMproject.controller;

import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.NMproject.dto.BookDTO;
import com.example.NMproject.service.BookService;

@Controller
@RequestMapping("api/books")
public class BookController {

	@Autowired
	private BookService bookService;

	@GetMapping("bookss")
	public String bookManagement() {
		return "BookManagement"; // Tham chiếu đến BookManagement.html trong thư mục /templates
	}

	@GetMapping("/allbooks")
	public ResponseEntity<List<BookDTO>> getAllBooks() {
		List<BookDTO> bookDTOs = bookService.getAllBooks();
		return ResponseEntity.ok(bookDTOs);
	}

	@PostMapping("/search")
	public ResponseEntity<List<BookDTO>> searchBooksByKeyword(@RequestBody Map<String, String> payload) {
		String keyword = payload.get("keyword");
		List<BookDTO> bookDTO = bookService.searchBooksByKeyword(keyword);
		return ResponseEntity.ok(bookDTO);
	}

	@PostMapping("/addbooks")
	public ResponseEntity<BookDTO> addBook(@RequestBody BookDTO bookDTO) {
		BookDTO addedBook = bookService.addBook(bookDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(addedBook);
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<BookDTO> updateBook(@PathVariable Long id, @RequestBody BookDTO bookDTO) {
		BookDTO updatedBook = bookService.updateBook(id, bookDTO);
		return ResponseEntity.ok(updatedBook);
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<BookDTO> deleteBookById(@PathVariable Long id) {
		// Kiểm tra xem sách có tồn tại hay không
		BookDTO bookDTO = bookService.getBookById(id); // Lấy thông tin sách nếu cần

		// Tiến hành xóa sách
		bookService.deleteBookById(id);

		// Trả về phản hồi giống như addBook
		return ResponseEntity.ok(bookDTO); // Trả về BookDTO sau khi xóa
	}

	@GetMapping("/detail/{id}")
	public ResponseEntity<BookDTO> getBookById(@PathVariable Long id) {
		BookDTO bookDTO = bookService.getBookById(id);
		return ResponseEntity.ok(bookDTO);
	}

	@GetMapping("/image/{filename:.+}")
	public ResponseEntity<org.springframework.core.io.Resource> getImage(@PathVariable String filename) {
		try {
			// Đọc ảnh từ thư mục /static/hinh_anh/anh_sach/
			java.nio.file.Path filePath = Paths.get("src/main/resources/static/hinh_anh/anh_sach/").resolve(filename)
					.normalize();

			// Tạo UrlResource từ đường dẫn
			UrlResource resource = new UrlResource(filePath.toUri());

			if (resource.exists() || resource.isReadable()) {
				// Xác định loại MIME phù hợp
				String contentType = filename.endsWith(".png") ? MediaType.IMAGE_PNG_VALUE : MediaType.IMAGE_JPEG_VALUE;
				return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).body(resource);
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().build();
		}
	}
}

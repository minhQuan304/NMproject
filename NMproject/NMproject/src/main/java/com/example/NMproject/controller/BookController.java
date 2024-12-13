package com.example.NMproject.controller;

import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
import com.example.NMproject.entity.Book;
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
		try {
			List<Book> books = bookService.getAllBooks();
			List<BookDTO> bookDTOs = books.stream()
					.map(book -> BookDTO.builder().bookId(book.getBookId()).title(book.getTitle())
							.category(book.getCategory()).author(book.getAuthor()).publishDate(book.getPublishDate())
							.imageLink(book.getImageLink()).description(book.getDescription())
							.quantityTotal(book.getQuantityTotal()).quantityValid(book.getQuantityValid())
							.rate(book.getRate()).build())
					.collect(Collectors.toList());
			return ResponseEntity.ok(bookDTOs);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	// Endpoint tìm kiếm sách theo từ khóa
	@PostMapping("/search")
	public ResponseEntity<List<BookDTO>> searchBooksByKeyword(@RequestBody Map<String, String> payload) {
		try {
			String keyword = payload.get("keyword");
			if (keyword == null || keyword.trim().isEmpty()) {
				return ResponseEntity.badRequest().body(Collections.emptyList());
			}
			List<BookDTO> books = bookService.searchBooksByKeyword(keyword).stream()
					.map(book -> BookDTO.builder().bookId(book.getBookId()).title(book.getTitle())
							.category(book.getCategory()).author(book.getAuthor()).publishDate(book.getPublishDate())
							.imageLink(book.getImageLink()).description(book.getDescription())
							.quantityTotal(book.getQuantityTotal()).quantityValid(book.getQuantityValid())
							.rate(book.getRate()).build())
					.collect(Collectors.toList());
			return ResponseEntity.ok(books);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList()); // Trả về danh
																											// sách rỗng
																											// nếu có
																											// lỗi
		}
	}

	@PostMapping("/addbooks")
	public ResponseEntity<BookDTO> addBook(@RequestBody BookDTO bookDTO) {
		try {
			if (bookDTO == null || bookDTO.getTitle() == null || bookDTO.getTitle().isEmpty()) {
				return ResponseEntity.badRequest().body(null); // Trả về lỗi nếu thông tin không hợp lệ
			}

			// Gọi service để thêm sách
			bookService.addBook(bookDTO);

			return ResponseEntity.status(HttpStatus.CREATED).body(bookDTO); // Trả về JSON của BookDTO
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // Trả về lỗi nếu có ngoại lệ
		}
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<BookDTO> updateBook(@PathVariable Long id, @RequestBody BookDTO bookDTO) {
		try {
			// Kiểm tra xem bookDTO có hợp lệ không
			if (bookDTO == null) {
				return ResponseEntity.badRequest().body(null); // Nếu bookDTO là null, trả về lỗi
			}

			// Gọi service để cập nhật thông tin sách
			BookDTO updatedBook = bookService.updateBook(id, bookDTO);

			// Trả về BookDTO đã cập nhật dưới dạng JSON
			return ResponseEntity.ok(updatedBook);

		} catch (Exception e) {
			// Trả về lỗi nếu có ngoại lệ
			return ResponseEntity.status(500).body(null);
		}
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Map<String, String>> deleteBookById(@PathVariable Long id) {
		try {
			// Gọi service để xóa sách theo id
			bookService.deleteBookById(id);

			// Trả về JSON thông báo kết quả xóa thành công
			Map<String, String> response = Collections.singletonMap("message", "Book deleted successfully");
			return ResponseEntity.ok(response); // Trả về mã trạng thái OK và thông báo JSON
		} catch (Exception e) {
			// Trả về thông báo lỗi nếu có vấn đề
			Map<String, String> errorResponse = Collections.singletonMap("error", "Book not found or failed to delete");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
		}
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

	// Thêm endpoint POST để tìm sách theo ID
	@GetMapping("/detail/{id}")
	public ResponseEntity<BookDTO> getBookById(@PathVariable Long id) {
		try {
			// Tìm sách theo ID từ service
			Book book = bookService.getBookById(id);

			// Kiểm tra nếu không tìm thấy sách
			if (book == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}

			// Chuyển đổi Book thành BookDTO và trả về
			BookDTO bookDTO = BookDTO.builder().bookId(book.getBookId()).title(book.getTitle())
					.category(book.getCategory()).author(book.getAuthor()).publishDate(book.getPublishDate())
					.imageLink(book.getImageLink()).description(book.getDescription())
					.quantityTotal(book.getQuantityTotal()).quantityValid(book.getQuantityValid()).rate(book.getRate())
					.build();

			return ResponseEntity.ok(bookDTO);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

}

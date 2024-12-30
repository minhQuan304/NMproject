package com.example.NMproject.controller;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.NMproject.dto.BookDTO;
import com.example.NMproject.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;

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

//	@PostMapping("/addbooks")
//	public ResponseEntity<BookDTO> addBook(@RequestBody BookDTO bookDTO) {
//		BookDTO addedBook = bookService.addBook(bookDTO);
//		return ResponseEntity.status(HttpStatus.CREATED).body(addedBook);
//	}
//	@PostMapping("/addbooks")
//	public ResponseEntity<BookDTO> addBook(@RequestParam("book") String bookJson,
//			@RequestParam("image") MultipartFile image) {
//		try {
//			// Chuyển đổi bookJson thành đối tượng BookDTO
//			ObjectMapper objectMapper = new ObjectMapper();
//			BookDTO bookDTO = objectMapper.readValue(bookJson, BookDTO.class);
//
//			// Lưu ảnh vào thư mục static/hinh_anh/anh_sach/
//			String imageFileName = StringUtils.cleanPath(image.getOriginalFilename());
//			Path imagePath = Paths.get("src/main/resources/static/hinh_anh/anh_sach/" + imageFileName);
//			Files.copy(image.getInputStream(), imagePath);
//
//			// Cập nhật đường dẫn ảnh vào đối tượng BookDTO
//			bookDTO.setImageLink("/hinh_anh/anh_sach/" + imageFileName);
//
//			// Thêm sách vào hệ thống
//			BookDTO addedBook = bookService.addBook(bookDTO);
//
//			// Trả về thông tin sách đã thêm
//			return ResponseEntity.status(HttpStatus.CREATED).body(addedBook);
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//		}
//	}
	@PostMapping("/addbooks")
	public ResponseEntity<BookDTO> addBook(@RequestParam("book") String bookJson,
			@RequestParam("image") MultipartFile image) {
		try {
			// Chuyển đổi bookJson thành đối tượng BookDTO
			ObjectMapper objectMapper = new ObjectMapper();
			BookDTO bookDTO = objectMapper.readValue(bookJson, BookDTO.class);

			// Lưu ảnh vào thư mục static/hinh_anh/anh_sach/
			String imageFileName = StringUtils.cleanPath(image.getOriginalFilename());
			Path imagePath = Paths.get("src/main/resources/static/hinh_anh/anh_sach/" + imageFileName);
			Files.copy(image.getInputStream(), imagePath);

			// Cập nhật đường dẫn ảnh vào đối tượng BookDTO
			bookDTO.setImageLink("/image/" + imageFileName);

			// Thêm sách vào hệ thống
			BookDTO addedBook = bookService.addBook(bookDTO);

			// Trả về thông tin sách đã thêm
			return ResponseEntity.status(HttpStatus.CREATED).body(addedBook);

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}

//	@PutMapping("/update/{id}")
//	public ResponseEntity<BookDTO> updateBook(@PathVariable Long id, @RequestParam("bookDTO") String bookJson,
//			@RequestParam("file") MultipartFile file) {
//		try {
//			// Chuyển đổi bookJson thành đối tượng BookDTO
//			ObjectMapper objectMapper = new ObjectMapper();
//			BookDTO bookDTO = objectMapper.readValue(bookJson, BookDTO.class);
//
//			// Xử lý file ảnh sách (lưu ảnh vào thư mục static/hinh_anh/anh_sach/)
//			String imageFileName = StringUtils.cleanPath(file.getOriginalFilename());
//			Path imagePath = Paths.get("src/main/resources/static/hinh_anh/anh_sach/" + imageFileName);
//			Files.copy(file.getInputStream(), imagePath);
//
//			// Cập nhật đường dẫn ảnh vào đối tượng BookDTO
//			bookDTO.setImageLink("/image/" + imageFileName);
//
//			// Gọi phương thức trong service để cập nhật sách
//			BookDTO updatedBook = bookService.updateBook(id, bookDTO);
//
//			// Trả về thông tin sách đã cập nhật
//			return ResponseEntity.ok(updatedBook);
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//		}
//	}
	@PutMapping("/update/{id}")
	public ResponseEntity<BookDTO> updateBook(@PathVariable Long id, @RequestParam("bookDTO") String bookJson,
			@RequestParam(value = "file", required = false) MultipartFile file) {
		try {
			// Chuyển đổi bookJson thành đối tượng BookDTO
			ObjectMapper objectMapper = new ObjectMapper();
			BookDTO bookDTO = objectMapper.readValue(bookJson, BookDTO.class);

			// Lấy thông tin sách hiện tại từ cơ sở dữ liệu
			BookDTO existingBook = bookService.getBookById(id);

			// Cập nhật các thông tin sách từ BookDTO
			existingBook.setTitle(bookDTO.getTitle());
			existingBook.setCategory(bookDTO.getCategory());
			existingBook.setAuthor(bookDTO.getAuthor());
			existingBook.setPublishDate(bookDTO.getPublishDate());
			existingBook.setDescription(bookDTO.getDescription());
			existingBook.setQuantityTotal(bookDTO.getQuantityTotal());
			existingBook.setQuantityValid(bookDTO.getQuantityValid());
			existingBook.setRate(bookDTO.getRate());

			// Xử lý file ảnh sách nếu có, nếu không có ảnh thì giữ lại đường dẫn cũ
			if (file != null && !file.isEmpty()) {
				String imageFileName = StringUtils.cleanPath(file.getOriginalFilename());
				Path imagePath = Paths.get("src/main/resources/static/hinh_anh/anh_sach/" + imageFileName);
				Files.copy(file.getInputStream(), imagePath);

				// Cập nhật đường dẫn ảnh mới vào đối tượng BookDTO
				existingBook.setImageLink("/image/" + imageFileName);
			} else {
				// Nếu không có ảnh mới, giữ lại đường link cũ của ảnh
				existingBook.setImageLink(existingBook.getImageLink());
			}

			// Cập nhật sách trong cơ sở dữ liệu
			bookService.updateBook(id, existingBook);

			// Trả về thông tin sách đã được cập nhật
			return ResponseEntity.ok(existingBook);

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
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

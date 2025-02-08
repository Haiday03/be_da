package com.lms.api;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import javax.validation.Valid;

import com.llq.springfilter.boot.Filter;
import com.lms.dto.*;
import com.lms.dto.exception.BookNotFoundException;
import com.lms.dto.exception.NotFoundException;
import com.lms.model.Book;
import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import com.lms.service.imp.AuthorServiceImp;
import com.lms.service.imp.BookServiceImp;
import com.lms.util.ApiPaths;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping(ApiPaths.BookCtrl.CTRL)
@CrossOrigin
@RequiredArgsConstructor
@MultipartConfig(
		fileSizeThreshold = 1024 * 1024, // Kích thước tối thiểu của file để được lưu trữ trong bộ nhớ tạm thời (đơn vị tính là byte)
		maxFileSize = 1024 * 1024 * 100, // Kích thước tối đa của mỗi file (đơn vị tính là byte)
		maxRequestSize = 1024 * 1024 * 100 // Kích thước tối đa của toàn bộ request chứa các file (đơn vị tính là byte)
)
public class BookRestController {

	private final AuthorServiceImp authorServiceImp;
	private final BookServiceImp bookServiceImp;

	@GetMapping()
	public ResponseEntity<List<BookDto>> getAll(@RequestParam("code") String code, @RequestParam("name") String name) throws NotFoundException {
		return ResponseEntity.ok(bookServiceImp.getAll(code, name));
	}
//
//	@GetMapping("/filter")
//	public ResponseEntity<List<BookDto>> getTopBooks() throws NotFoundException {
//
//		List<BookDto> filter = bookServiceImp.getAll().stream().filter(f-> f.getRating()>=4).collect(Collectors.toList());
//		return ResponseEntity.ok(filter);
//	}

	// localhost:8182/api/book/5
	@GetMapping("/{id}")
	public ResponseEntity<BookOneDto> getById(@PathVariable(name = "id", required = true) Long id)
			throws NotFoundException {
		return ResponseEntity.ok(bookServiceImp.getOne(id).orElseThrow(() -> new BookNotFoundException(id)));
	}

	@GetMapping("/find/{name}")
	public ResponseEntity<List<BookDto>> findByName(@PathVariable(name = "name", required = true) String name)
			throws NotFoundException {

		if(name.isEmpty()){
			return ResponseEntity.ok(bookServiceImp.getAll("", ""));
		}
		return ResponseEntity.ok(bookServiceImp.SearchBooksByName(name));
	}

	@GetMapping("/inventoryStatus/{quantity}")
	public String inventoryStatus(@PathVariable(name = "quantity", required = true) int quantity )
			throws NotFoundException {
			if(quantity<25){
				return "LOWSTOCK";
			}
			else if(quantity>=25){
				return "INSTOCK";
			}
			return "NOSTOCK";

	}

	@GetMapping("/recentSales")
	public ResponseEntity<BookForDashboard[]> getRecentSales() throws StripeException {
		return ResponseEntity.ok(this.bookServiceImp.getRecentSales());
	}

	@GetMapping("/slope")
	public ResponseEntity.BodyBuilder getSlope()
	{
		this.bookServiceImp.Slope();
		return ResponseEntity.ok();
	}

	@GetMapping("/{id}/recommended")
	public BookRecDto[] getRecommended(@PathVariable(required = true) Long id)
	{
		return this.bookServiceImp.getRecommended(this.bookServiceImp.getOne(id).get());
	}


	@PostMapping()
	public ResponseEntity<BookOneDto> createProject(@Valid @RequestBody BookOneDto bookOneDto) throws Exception {
		return ResponseEntity.ok(bookServiceImp.save(bookOneDto));
	}
	@PutMapping("/{id}")
	public ResponseEntity<BookDto> updateBook(@PathVariable(name = "id", required = true) Long id,
			@Valid @RequestBody BookDto bookOneDto) throws NotFoundException {

		return ResponseEntity.ok(bookServiceImp.update(id, bookOneDto));
	}
	@DeleteMapping("/{id}")
	public ResponseEntity<Boolean> deleteBook(@PathVariable(name = "id", required = true) Long id)
			throws NotFoundException {
		return ResponseEntity.ok(bookServiceImp.delete(id));
	}

	@DeleteMapping()
	public ResponseEntity<Boolean> deleteList(@RequestBody BookDto[] dtos)
			throws NotFoundException {
		return ResponseEntity.ok(bookServiceImp.deleteList(dtos));
	}

	@GetMapping("/get/page")
	public ResponseEntity<Page<BookDto>> pageQuery(@Filter Specification<Book> spec, @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
		Page<BookDto> bookDtoPage = bookServiceImp.findByCondition(spec, pageable);
		return ResponseEntity.ok(bookDtoPage);
	}


	@PostMapping("/upload/image")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<String> uploadImage(HttpServletRequest request) throws ServletException, IOException {
		try {
			String uploadPath = "C:\\Projects\\Bao_cao_do_an\\Library-Management\\frontend\\src\\assets\\uploads";

			File uploadDir = new File(uploadPath);
			if (!uploadDir.exists()) {
				uploadDir.mkdir();
			}

			Part filePart = request.getPart("file");

			String fileName = getFileName(filePart);
			String prefix = fileName.substring(0, fileName.lastIndexOf(".") - 1);
			String suffixes = fileName.substring(fileName.lastIndexOf("."));
			long millis = new java.util.Date().getTime();
			String fileNameSaved = prefix + millis + suffixes;
			String filePath = uploadPath + File.separator + fileNameSaved;
			filePart.write(filePath);

//			String message = "Tải tệp lên thành công!";
			String message = "assets/uploads/" + fileNameSaved;
			return ResponseEntity.status(HttpStatus.OK).body(message);
		} catch (Exception e){
			String message = "Tải tệp lên không thành công!";
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
		}
	}

	// Lấy tên file
	public static String getFileName(Part part) {
		for (String contentDisposition : part.getHeader("content-disposition").split(";")) {
			if (contentDisposition.trim().startsWith("filename")) {
				return contentDisposition.substring(contentDisposition.indexOf('=') + 1).trim().replace("\"", "");
			}
		}
		return null;
	}
}

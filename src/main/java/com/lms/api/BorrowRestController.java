package com.lms.api;

import com.llq.springfilter.boot.Filter;
import com.lms.dto.BorrowDto;
import com.lms.dto.BorrowSearchDto;
import com.lms.dto.exception.NotFoundException;
import com.lms.model.Borrow;
import com.lms.service.ExcelService;
import com.lms.service.imp.BorrowServiceImp;
import com.lms.util.ApiPaths;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(ApiPaths.BorrowCtrl.CTRL)
@CrossOrigin
@RequiredArgsConstructor
public class BorrowRestController {

	private final BorrowServiceImp borrowServiceImp;
	private final ExcelService excelService;

	@GetMapping()
	public ResponseEntity<List<BorrowDto>> getAll() throws NotFoundException {
		List<BorrowDto> borrowDtos = borrowServiceImp.getAll();
		return ResponseEntity.ok(borrowDtos);
	}

	// http://localhost:8081/api/borrow/2
	@GetMapping("/{id}")
	public ResponseEntity<BorrowDto> getOneBorrow(@PathVariable(name = "id", required = true) Long id)
			throws NotFoundException {
		return ResponseEntity.ok(borrowServiceImp.getOne(id));
	}

	@PostMapping()
	public ResponseEntity<BorrowDto> createBorrow(@Valid @RequestBody BorrowDto borrowDto) {
		return ResponseEntity.ok(borrowServiceImp.save(borrowDto));
	}

	@PutMapping("/{id}")
	public ResponseEntity<BorrowDto> updateBorrow(@PathVariable(name = "id", required = true) Long id,
			@Valid @RequestBody BorrowDto borrowDto) throws NotFoundException {
		return ResponseEntity.ok(borrowServiceImp.update(id, borrowDto));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Boolean> deleteBorrow(@PathVariable(name = "id", required = true) Long id)
			throws NotFoundException {
		return ResponseEntity.ok(borrowServiceImp.delete(id));
	}

	@DeleteMapping
	public ResponseEntity<Boolean> deleteListBorrows(@RequestBody long[] dtos)
			throws NotFoundException {
		return ResponseEntity.ok(borrowServiceImp.deleteList(dtos));
	}

	@DeleteMapping("/del/review/{id}")
	public ResponseEntity<Boolean> deleteReview(@PathVariable(name = "id", required = true) Long id)
			throws NotFoundException {
		return ResponseEntity.ok(borrowServiceImp.deleteReview(id));
	}

	@DeleteMapping("/del/list-review")
	public ResponseEntity<Boolean> deleteListReviews(@RequestBody long[] dtos)
			throws NotFoundException {
		return ResponseEntity.ok(borrowServiceImp.deleteListReviews(dtos));
	}

	@GetMapping("/get/page")
	public ResponseEntity<Page<BorrowDto>> pageQuery(@Filter Specification<Borrow> spec, @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
		Page<BorrowDto> bookDtoPage = borrowServiceImp.findByCondition(spec, pageable);
		return ResponseEntity.ok(bookDtoPage);
	}

	@PutMapping("/status/{id}")
	public ResponseEntity<BorrowDto> updateBorrowStatus(@PathVariable(name = "id", required = true) Long id) throws Exception {
		return ResponseEntity.ok(borrowServiceImp.updateBorrowStatus(id));
	}

	@GetMapping("/get/dasboard")
	public ResponseEntity<Map<String, Object>> getDashboard() {
		Map<String, Object> result = borrowServiceImp.getDashboardData();
		return ResponseEntity.ok(result);
	}

	@GetMapping("/export/excel")
	public ResponseEntity<byte[]> exportToExcel(@Filter Specification<Borrow> spec) throws IOException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.setContentDispositionFormData("attachment", "my-excel-file.xlsx");

		return new ResponseEntity<>(excelService.generateExcelBytes(spec), headers, HttpStatus.OK);
	}

	@GetMapping("/export/review-excel")
	public ResponseEntity<byte[]> exportReviewExcel(@Filter Specification<Borrow> spec) throws IOException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.setContentDispositionFormData("attachment", "my-excel-file.xlsx");

		return new ResponseEntity<>(excelService.generateReviewExcelBytes(spec), headers, HttpStatus.OK);
	}

	// http://localhost:8081/api/borrow/review/{bookId}
	@GetMapping("/review/{bookId}")
	public ResponseEntity<List<Borrow>> getReviewsByBookId(@PathVariable(name = "bookId", required = true) Long bookId) {
		return ResponseEntity.ok(borrowServiceImp.getReviewsByBook(bookId));
	}

	@PostMapping("/search")
	public ResponseEntity<Page<BorrowDto>> search(@RequestBody BorrowSearchDto newSearchDto) {
		Page<BorrowDto> bookDtoPage = borrowServiceImp.search(newSearchDto);
		return ResponseEntity.ok(bookDtoPage);
	}
}

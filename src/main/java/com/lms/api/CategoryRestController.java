package com.lms.api;

import com.llq.springfilter.boot.Filter;
import com.lms.dto.CategoryDto;
import com.lms.dto.CategoryDto;
import com.lms.dto.CategorySearchDto;
import com.lms.dto.exception.NotFoundException;
import com.lms.model.Category;
import com.lms.service.imp.CategoryServiceImpl;
import com.lms.util.ApiPaths;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping(ApiPaths.CategoryCtrl.CTRL)
@CrossOrigin
@RequiredArgsConstructor
public class CategoryRestController {

	private final CategoryServiceImpl categoryServiceImpl;

	@GetMapping()
	public ResponseEntity<List<CategoryDto>> getAll() throws NotFoundException {
		List<CategoryDto> listCategory = categoryServiceImpl.getAll();
		return ResponseEntity.ok(listCategory);
	}

	@GetMapping("/{id}")
	public ResponseEntity<CategoryDto> getOne(@PathVariable(name = "id", required = true) Long id)
			throws NotFoundException {
		return ResponseEntity.ok(categoryServiceImpl.getOne(id));
	}

	@PostMapping()
	public ResponseEntity<CategoryDto> create(@Valid @RequestBody CategoryDto dto) {
		return ResponseEntity.ok(categoryServiceImpl.save(dto));
	}

	@PutMapping("/{id}")
	public ResponseEntity<CategoryDto> updateCategory(@PathVariable(name = "id", required = true) Long id,
			@Valid @RequestBody CategoryDto dto) throws NotFoundException {
		return ResponseEntity.ok(categoryServiceImpl.update(id, dto));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Boolean> deleteOne(@PathVariable(name = "id", required = true) Long id)
			throws NotFoundException {
		return ResponseEntity.ok(categoryServiceImpl.delete(id));
	}

	@DeleteMapping()
	public ResponseEntity<Boolean> deleteList(@RequestBody CategoryDto[] dtos)
			throws NotFoundException {
		return ResponseEntity.ok(categoryServiceImpl.deleteList(dtos));
	}

	@GetMapping("/get/page")
	public ResponseEntity<Page<CategoryDto>> pageQuery(@Filter Specification<Category> spec, @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
		Page<CategoryDto> bookDtoPage = categoryServiceImpl.findByCondition(spec, pageable);
		return ResponseEntity.ok(bookDtoPage);
	}

	@PostMapping("/search")
	public ResponseEntity<Page<CategoryDto>> search(@RequestBody CategorySearchDto categorySearchDto) {
		Page<CategoryDto> bookDtoPage = categoryServiceImpl.search(categorySearchDto);
		return ResponseEntity.ok(bookDtoPage);
	}

	@GetMapping("/get/top-5")
	public ResponseEntity<List<Category>> getTop5() {
		return ResponseEntity.ok(categoryServiceImpl.getTop5());
	}
}

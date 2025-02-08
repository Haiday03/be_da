package com.lms.api;

import com.lms.dto.ReviewDto;
import com.lms.dto.exception.NotFoundException;
import com.lms.service.imp.ReviewServiceImp;
import com.lms.util.ApiPaths;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping(ApiPaths.ReviewCtrl.CTRL)
@CrossOrigin
public class ReviewRestController {

	private final ReviewServiceImp reviewServiceImp;

	public ReviewRestController(ReviewServiceImp reviewServiceImp) {
		super();
		this.reviewServiceImp = reviewServiceImp;
	}

	// http://localhost:8081/api/new
	@GetMapping()
	public ResponseEntity<List<ReviewDto>> getAll() throws NotFoundException {
		List<ReviewDto> reviews = reviewServiceImp.getAll();
		return ResponseEntity.ok(reviews);
	}

	// http://localhost:8081/api/new/2
	@GetMapping("/{id}")
	public ResponseEntity<ReviewDto> getOne(@PathVariable(name = "id", required = true) Long id)
			throws NotFoundException {
		return ResponseEntity.ok(reviewServiceImp.getOne(id));
	}

	@PostMapping()
	public ResponseEntity<ReviewDto> create(@Valid @RequestBody ReviewDto reviewDto) {
		return ResponseEntity.ok(reviewServiceImp.save(reviewDto));
	}

	@PutMapping("/{id}")
	public ResponseEntity<ReviewDto> update(@PathVariable(name = "id", required = true) Long id,
			@Valid @RequestBody ReviewDto newDto) throws NotFoundException {
		return ResponseEntity.ok(reviewServiceImp.update(id, newDto));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Boolean> delete(@PathVariable(name = "id", required = true) Long id)
			throws NotFoundException {
		return ResponseEntity.ok(reviewServiceImp.delete(id));
	}

}

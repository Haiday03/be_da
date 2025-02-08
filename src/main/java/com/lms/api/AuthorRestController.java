package com.lms.api;

import java.util.List;

import javax.validation.Valid;

import com.llq.springfilter.boot.Filter;
import com.lms.dto.*;
import com.lms.dto.exception.NotFoundException;
import com.lms.model.Author;
import com.lms.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.lms.service.imp.AuthorServiceImp;
import com.lms.util.ApiPaths;


@RestController
@RequestMapping(ApiPaths.AuthorCtrl.CTRL)
@CrossOrigin
public class AuthorRestController {

	private final AuthorServiceImp authorServiceImp;

	public AuthorRestController(AuthorServiceImp authorServiceImp) {
		super();
		this.authorServiceImp = authorServiceImp;
	}

	// http://localhost:8081/api/author
	@GetMapping()
	public ResponseEntity<List<AuthorDto>> getAll() throws NotFoundException {
		List<AuthorDto> authorDtos = authorServiceImp.getAll();
		return ResponseEntity.ok(authorDtos);
	}

	// http://localhost:8081/api/author/2
	@GetMapping("/{id}")
	public ResponseEntity<AuthorOneDto> getOneAuthor(@PathVariable(name = "id", required = true) Long id)
			throws NotFoundException {
		return ResponseEntity.ok(authorServiceImp.getOne(id));
	}

	@PostMapping()
	public ResponseEntity<AuthorDto> createAuthor(@Valid @RequestBody AuthorDto authorDto) {
		return ResponseEntity.ok(authorServiceImp.save(authorDto));
	}

	@PutMapping("/{id}")
	public ResponseEntity<AuthorDto> updateAuthor(@PathVariable(name = "id", required = true) Long id,
			@Valid @RequestBody AuthorDto authorDto) throws NotFoundException {
		return ResponseEntity.ok(authorServiceImp.update(id, authorDto));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Boolean> deleteBook(@PathVariable(name = "id", required = true) Long id)
			throws NotFoundException {
		return ResponseEntity.ok(authorServiceImp.delete(id));
	}

	@DeleteMapping()
	public ResponseEntity<Boolean> deleteList(@RequestBody AuthorDto[] dtos)
			throws NotFoundException {
		return ResponseEntity.ok(authorServiceImp.deleteList(dtos));
	}

	@GetMapping("/get/page")
	public ResponseEntity<Page<AuthorDto>> pageQuery(@Filter Specification<Author> spec, @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
		Page<AuthorDto> bookDtoPage = authorServiceImp.findByCondition(spec, pageable);
		return ResponseEntity.ok(bookDtoPage);
	}

}

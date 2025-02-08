package com.lms.api;

import com.llq.springfilter.boot.Filter;
import com.lms.dto.NewDto;
import com.lms.dto.exception.NotFoundException;
import com.lms.model.NewEntity;
import com.lms.service.imp.NewServiceImp;
import com.lms.util.ApiPaths;
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
@RequestMapping(ApiPaths.NewCtrl.CTRL)
@CrossOrigin
public class NewRestController {

	private final NewServiceImp newServiceImp;

	public NewRestController(NewServiceImp newServiceImp) {
		super();
		this.newServiceImp = newServiceImp;
	}

	// http://localhost:8081/api/new
	@GetMapping()
	public ResponseEntity<List<NewDto>> getAll() throws NotFoundException {
		List<NewDto> newDtos = newServiceImp.getAll();
		return ResponseEntity.ok(newDtos);
	}


	// http://localhost:8081/api/author/find?name=name
//	@GetMapping("/find")
//	public ResponseEntity<List<NewDto>> findAllByName(@RequestParam String name) throws NotFoundException {
//		List<NewDto> authorDtos = newServiceImp.findAllByName(name);
//		if(authorDtos.isEmpty()){
//			return ResponseEntity.ok(newServiceImp.getAll());
//		}
//		return ResponseEntity.ok(authorDtos);
//	}

	// http://localhost:8081/api/new/2
	@GetMapping("/{id}")
	public ResponseEntity<NewDto> getOne(@PathVariable(name = "id", required = true) Long id)
			throws NotFoundException {
		return ResponseEntity.ok(newServiceImp.getOne(id));
	}

	@PostMapping()
	public ResponseEntity<NewDto> create(@Valid @RequestBody NewDto authorDto) {
		return ResponseEntity.ok(newServiceImp.save(authorDto));
	}

	@PutMapping("/{id}")
	public ResponseEntity<NewDto> update(@PathVariable(name = "id", required = true) Long id,
			@Valid @RequestBody NewDto newDto) throws NotFoundException {
		return ResponseEntity.ok(newServiceImp.update(id, newDto));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Boolean> delete(@PathVariable(name = "id", required = true) Long id)
			throws NotFoundException {
		return ResponseEntity.ok(newServiceImp.delete(id));
	}

	@GetMapping("/get/page")
	public ResponseEntity<Page<NewDto>> pageQuery(@Filter Specification<NewEntity> spec, @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
		Page<NewDto> bookDtoPage = newServiceImp.findByCondition(spec, pageable);
		return ResponseEntity.ok(bookDtoPage);
	}
}

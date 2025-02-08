package com.lms.api;

import com.llq.springfilter.boot.Filter;
import com.lms.dto.*;
import com.lms.dto.PublisherDto;
import com.lms.dto.exception.NotFoundException;
import com.lms.model.Author;
import com.lms.model.Publisher;
import com.lms.service.imp.PublisherServiceImp;
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
@RequestMapping(ApiPaths.PublisherCtrl.CTRL)
@CrossOrigin
public class PublisherRestController {

    private final PublisherServiceImp publisherServiceImp;

    public PublisherRestController(PublisherServiceImp publisherServiceImp) {
        this.publisherServiceImp = publisherServiceImp;
    }

    // http://localhost:8182/api/publisher
    @GetMapping()
    public ResponseEntity<List<PublisherDto>> getAll() throws NotFoundException {
        List<PublisherDto> publisherDtos = publisherServiceImp.getAll();
        return ResponseEntity.ok(publisherDtos);
    }

    // http://localhost:8182/api/publisher/find?name=name
//    @GetMapping("/find")
//    public ResponseEntity<List<PublisherDto>> findAllByName(@RequestParam String name) throws NotFoundException {
//        List<PublisherDto> publisherDtos = publisherServiceImp.findAllByName(name);
//        if(publisherDtos.isEmpty()){
//            return ResponseEntity.ok(publisherServiceImp.getAll());
//        }
//        return ResponseEntity.ok(publisherDtos);
//    }

    // http://localhost:8182/api/publisher/2
    @GetMapping("/{id}")
    public ResponseEntity<PublisherOneDto> getOnePublisher(@PathVariable(name = "id", required = true) Long id)
            throws NotFoundException {
        return ResponseEntity.ok(publisherServiceImp.getOne(id));
    }

    @PostMapping()
    public ResponseEntity<PublisherDto> createPublisher(@Valid @RequestBody PublisherDto publisherDto) {
        return ResponseEntity.ok(publisherServiceImp.save(publisherDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PublisherUpdateDto> updatePublisher(@PathVariable(name = "id", required = true) Long id,
                                                        @Valid @RequestBody PublisherUpdateDto publisherUpdateDto) throws NotFoundException {
        return ResponseEntity.ok(publisherServiceImp.update(id, publisherUpdateDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteBook(@PathVariable(name = "id", required = true) Long id)
            throws NotFoundException {
        return ResponseEntity.ok(publisherServiceImp.delete(id));
    }

    @DeleteMapping()
    public ResponseEntity<Boolean> deleteList(@RequestBody PublisherDto[] dtos)
            throws NotFoundException {
        return ResponseEntity.ok(publisherServiceImp.deleteList(dtos));
    }

    @GetMapping("/get/page")
    public ResponseEntity<Page<PublisherDto>> pageQuery(@Filter Specification<Publisher> spec, @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<PublisherDto> bookDtoPage = publisherServiceImp.findByCondition(spec, pageable);
        return ResponseEntity.ok(bookDtoPage);
    }
}

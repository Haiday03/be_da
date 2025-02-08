package com.lms.service;

import com.lms.dto.ReviewDto;
import com.lms.dto.exception.NotFoundException;
import com.lms.util.TPage;
import org.springframework.data.domain.Pageable;

import javax.validation.Valid;
import java.util.List;

public interface ReviewService {
	public ReviewDto save(ReviewDto ReviewDto);
	public List<ReviewDto> getAll() throws NotFoundException;
	public TPage<ReviewDto> getAllPageable(Pageable pageable) throws NotFoundException;
//	public List<ReviewDto> findAllByName(String name) throws NotFoundException ;
	public ReviewDto update(Long id, @Valid ReviewDto reviewDto) throws NotFoundException;
	public ReviewDto getOne(Long id) throws NotFoundException;
	public Boolean delete(Long id) throws NotFoundException ;
}

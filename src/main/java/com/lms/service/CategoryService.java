package com.lms.service;

import com.lms.dto.CategoryDto;
import com.lms.dto.exception.NotFoundException;

import javax.validation.Valid;
import java.util.List;

public interface CategoryService {
	CategoryDto save(CategoryDto dto);
	List<CategoryDto> getAll() throws NotFoundException;
	CategoryDto update(Long id, @Valid CategoryDto dto) throws NotFoundException;
	CategoryDto getOne(Long id) throws NotFoundException;
	Boolean delete(Long id) throws NotFoundException;
	Boolean deleteList(CategoryDto[] dtos) throws NotFoundException;
}

package com.lms.service;

import com.lms.dto.NewDto;
import com.lms.dto.AuthorOneDto;
import com.lms.dto.AuthorUpdateDto;
import com.lms.dto.exception.NotFoundException;
import com.lms.util.TPage;
import org.springframework.data.domain.Pageable;

import javax.validation.Valid;
import java.util.List;

public interface NewService {
	public NewDto save(NewDto NewDto);
	public List<NewDto> getAll() throws NotFoundException;
	public TPage<NewDto> getAllPageable(Pageable pageable) throws NotFoundException;
//	public List<NewDto> findAllByName(String name) throws NotFoundException ;
	public NewDto update(Long id, @Valid NewDto newDto) throws NotFoundException;
	public NewDto getOne(Long id) throws NotFoundException;
	public Boolean delete(Long id) throws NotFoundException ;
}

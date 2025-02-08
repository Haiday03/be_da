package com.lms.service;

import com.lms.dto.BorrowDto;
import com.lms.dto.exception.NotFoundException;

import javax.validation.Valid;
import java.util.List;

public interface BorrowService {
	BorrowDto save(BorrowDto borrowDto);
	List<BorrowDto> getAll() throws NotFoundException;
	BorrowDto update(Long id, @Valid BorrowDto borrowDto) throws NotFoundException;
	BorrowDto getOne(Long id) throws NotFoundException;
	Boolean delete(Long id) throws NotFoundException;
}

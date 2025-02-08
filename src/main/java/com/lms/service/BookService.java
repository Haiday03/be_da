package com.lms.service;

import java.util.List;
import java.util.Optional;

import com.lms.dto.*;
import com.lms.dto.exception.NotFoundException;

import com.lms.model.Book;


public interface BookService {
	public BookOneDto save(BookOneDto bookOneDto) throws Exception;
	public List<BookDto> getAll(String code, String name) throws NotFoundException;
	public BookDto update(Long id, BookDto bookDto) throws NotFoundException;
	public Optional<BookOneDto> getOne(Long id) throws NotFoundException;

	public Optional<Book> getById(Long id) throws NotFoundException;
	public Boolean delete(Long id) throws NotFoundException;
	Boolean deleteList(BookDto[] dtos) throws NotFoundException;
	public List<BookDto> SearchBooksByName(String name) throws NotFoundException;

	public void Slope();

	public BookRecDto[] getRecommended(BookOneDto bookOneDto);
	public BookForDashboard[] getRecentSales();

}

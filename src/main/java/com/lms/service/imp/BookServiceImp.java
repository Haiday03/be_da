package com.lms.service.imp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.llq.springfilter.boot.Filter;
import com.lms.dto.*;
import com.lms.dto.exception.BookNotFoundException;
import com.lms.dto.exception.LogicalException;
import com.lms.dto.exception.NotFoundException;
import com.lms.ml.SlopeOne;
import com.lms.model.Borrow;
import com.lms.model.Category;
import com.lms.repository.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lms.model.Author;
import com.lms.model.Book;
import com.lms.service.BookService;

@Service
@Transactional
@RequiredArgsConstructor
public class BookServiceImp implements BookService {
	private final ModelMapper modelMapper;
	private final UserRepository userRepository;
	private final AuthorRepository authorRepository;
	private final BookRepository bookRepository;
	private final BorrowRepository borrowRepository;

	public BookOneDto save(BookOneDto bookOneDto) throws Exception {

		if(bookRepository.findByName(bookOneDto.getName()).size()>0){
			throw new Exception("Book already exist");
		}
		Book book = modelMapper.map(bookOneDto, Book.class);

		bookRepository.save(book);

		bookOneDto.setId(book.getId());
		return bookOneDto;
	}

	public List<BookDto> getAll(String code, String name) throws NotFoundException {
		List<Book> books = bookRepository.findAll();
		Optional<Book> book = bookRepository.findById(22L);
//		List<Book> books = bookRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
		if (books.size() < 1) {
//			throw new NotFoundException("No book");
			return new ArrayList<>();
		}
		BookDto[] bookDtos = modelMapper.map(books, BookDto[].class);
		return Arrays.asList(bookDtos);
	}

	@Transactional
	public BookDto update(Long id, BookDto bookDto) throws NotFoundException {

		Optional<Book> bookOpt = bookRepository.findById(id);
		if (!bookOpt.isPresent()) {
			throw new NotFoundException("Không tìm thấy bản ghi với id: " + id);
		}
		Optional<Author> author = authorRepository.findById(bookDto.getAuthorId());
		if (!author.isPresent()) {
			throw new NotFoundException("Không tìm thấy tác giả với id : " + bookDto.getAuthorId());
		}
		checkQuantityUpdated(bookDto.getQuantity(), bookOpt);
		Book book = modelMapper.map(bookDto, Book.class);
//		Optional<Author> authorOpt = authorRepository.findById(bookUpdateDto.getAuthorId());
//		book.setAuthor(authorOpt.get());
//		Optional<Publisher> publisherOpt = publisherRepository.findById(bookUpdateDto.getPublisherId());
//		book.setPublisher(publisherOpt.get());

		bookRepository.save(book);

		return bookDto;
	}

	private void checkQuantityUpdated(int quantity, Optional<Book> bookOpt){
		if(quantity < bookOpt.get().getLoaned()){
			throw new LogicalException("Tổng số lượng sách không thể nhỏ hơn số lượng đã cho mượn");
		}
	}

	public Optional<BookOneDto> getOne(Long id) throws NotFoundException {

		Optional<Book> book = bookRepository.findById(id);
		if (!book.isPresent()) {
			throw new NotFoundException("Không tìm thấy bản ghi với id: " + id);
		}
		BookOneDto bookOneDto = modelMapper.map(book.get(), BookOneDto.class);
		bookOneDto.setId(id);
//		bookOneDto.setAuthorId(book.get().getAuthor().getId());
//		bookOneDto.setPublisherId(book.get().getPublisher().getId());

		return Optional.of(bookOneDto);

	}

	@Override
	public Optional<Book> getById(Long id) throws NotFoundException {
		return Optional.of(bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id)));
	}

	public Boolean delete(Long id) throws NotFoundException {
		Optional<Book> book = bookRepository.findById(id);
		if (!book.isPresent()) {
			throw new NotFoundException("Không tìm thấy bản ghi với id: " + id);
		}
		List<Borrow> borrows = borrowRepository.findByBookId(id);
		if(borrows.size() > 0){
			throw new LogicalException("Bản ghi đang có ràng buộc về dữ liệu!");
		}
		bookRepository.deleteById(id);
		return true;

	}

	@Override
	public Boolean deleteList(BookDto[] dtos) throws NotFoundException {
		Book[] entities = modelMapper.map(dtos, Book[].class);
		for(int i = 0; i < dtos.length; i++){
			List<Borrow> borrows = borrowRepository.findByBookId(dtos[i].getId());
			if(borrows.size() > 0){
				throw new LogicalException("Bản ghi đang có ràng buộc về dữ liệu!");
			}
		}
		bookRepository.deleteAll(Arrays.asList(entities));
		return true;
	}

	public List<BookDto> SearchBooksByName(String name) throws NotFoundException {
		List<Book> books = bookRepository.SearchBooksByName(name.trim());
		if (books.size() < 1) {
			throw new NotFoundException("Sách không tồn tại");
		}
		BookDto[] bookDtos = modelMapper.map(books, BookDto[].class);
		return Arrays.asList(bookDtos);
	}

	@Override
	public void Slope() {
		SlopeOne.slopeOne(this.userRepository.findAll().size());
	}

	public BookRecDto[] getRecommended(BookOneDto bookOneDto)
	{
		BookRecDto[] bookDtos = modelMapper.map(this.bookRepository.getRecommended(bookOneDto.getCategoryId(),bookOneDto.getId()), BookRecDto[].class);
		return bookDtos;
	}
	@Override
	public BookForDashboard[] getRecentSales() {
		List<Book> bookOneDtos= this.bookRepository.getRecentSales();
		BookForDashboard[] bookForDashboards = modelMapper.map(bookOneDtos, BookForDashboard[].class);
		return  bookForDashboards;
	}

	public Page<BookDto> findByCondition(@Filter Specification<Book> spec, Pageable pageable) {
		Page<Book> entityPage = bookRepository.findAll(spec, pageable);
		List<Book> entities = entityPage.getContent();
		return new PageImpl<>(modelMapper.map(entities, new TypeToken<List<BookDto>>() {}.getType()), pageable, entityPage.getTotalElements());
	}
}

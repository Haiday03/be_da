package com.lms.service.imp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import javax.validation.Valid;

import com.llq.springfilter.boot.Filter;
import com.lms.dto.*;
import com.lms.dto.exception.LogicalException;
import com.lms.dto.exception.NotFoundException;
import com.lms.model.Book;
import com.lms.model.Category;
import com.lms.repository.BookRepository;
import com.lms.util.Utils;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.lms.model.Author;
import com.lms.repository.AuthorRepository;
import com.lms.repository.UserRepository;
import com.lms.service.AuthorService;
import com.lms.util.TPage;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthorServiceImp implements AuthorService{
	private final ModelMapper modelMapper;
	private final AuthorRepository authorRepository;
	private final BookRepository bookRepository;

	public AuthorDto save(AuthorDto authorDto) {
		Author authorChecked = authorRepository.findByEmail(authorDto.getEmail());
		if (authorChecked != null) {
			throw new IllegalArgumentException("User email already exist");
		}
		Author author = modelMapper.map(authorDto, Author.class);
		authorRepository.save(author);
		authorDto.setId(author.getId());
		return authorDto;
	}

	public List<AuthorDto> getAll() {
		List<Author> authors = authorRepository.findAll();
		if (authors.size() < 1) {
			throw new NotFoundException("Author don't already exist");
		}
		AuthorDto[] authorDtos = modelMapper.map(authors, AuthorDto[].class);

		return Arrays.asList(authorDtos);
	}

	public TPage<AuthorDto> getAllPageable(Pageable pageable) throws NotFoundException {
		try {
			Page<Author> page = authorRepository.findAll(PageRequest.of(pageable.getPageNumber(),
					pageable.getPageSize(), Sort.by(Sort.Direction.ASC, "id")));
			// Page<Author> page=authorRepository.findAll(pageable);
			TPage<AuthorDto> tPage = new TPage<AuthorDto>();
			AuthorDto[] authorDtos = modelMapper.map(page.getContent(), AuthorDto[].class);

			tPage.setStat(page, Arrays.asList(authorDtos));
			return tPage;
		} catch (Exception e) {
			throw new NotFoundException("User email doesn't exist : " + e);
		}
	}

	public AuthorDto update(Long id, @Valid AuthorDto authorDto) throws NotFoundException {
		Optional<Author> authorOpt = authorRepository.findById(id);
		if (!authorOpt.isPresent()) {
			throw new NotFoundException("Không tìm thấy bản ghi với id: : " + id);
		}
		Author author = modelMapper.map(authorDto, Author.class);
		author.setId(id);
		authorRepository.save(author);
		authorDto.setId(author.getId());
		return authorDto;

	}

	public AuthorOneDto getOne(Long id) throws NotFoundException {

		Optional<Author> author = authorRepository.findById(id);
		if (!author.isPresent()) {
			throw new NotFoundException("Không tìm thấy bản ghi với id: : " + id);
		}

		AuthorOneDto authorOneDto = modelMapper.map(author.get(), AuthorOneDto.class);
		authorOneDto.setId(id);
		return authorOneDto;

	}

	public Boolean delete(Long id) throws NotFoundException {
		Optional<Author> author = authorRepository.findById(id);
		if (!author.isPresent()) {
			throw new NotFoundException("Không tìm thấy bản ghi với id: : " + id);
		}
		List<Book> books = bookRepository.findByAuthorId(id);
		if(books.size() > 0){
			throw new LogicalException("Bản ghi đang có ràng buộc về dữ liệu!");
		}
		authorRepository.deleteById(id);
		return true;
	}

	public Boolean deleteList(AuthorDto[] dtos) throws NotFoundException {
		Author[] entities = modelMapper.map(dtos, Author[].class);
		for(int i = 0; i < dtos.length; i++){
			List<Book> books = bookRepository.findByAuthorId(dtos[i].getId());
			if(books.size() > 0){
				throw new LogicalException("Bản ghi đang có ràng buộc về dữ liệu!");
			}
		}
		authorRepository.deleteAll(Arrays.asList(entities));
		return true;
	}

	public Page<AuthorDto> findByCondition(@Filter Specification<Author> spec, Pageable pageable) {
		Page<Author> entityPage = authorRepository.findAll(spec, pageable);
		List<Author> entities = entityPage.getContent();
		return new PageImpl<>(modelMapper.map(entities, new TypeToken<List<AuthorDto>>() {}.getType()), pageable, entityPage.getTotalElements());
	}

	public Page<AuthorDto> search(AuthorSearchDto authorSearchDto) {
		Sort sort = Utils.generatedSort(authorSearchDto.getSort());
		Pageable pageable = PageRequest.of(authorSearchDto.getPage(), authorSearchDto.getLimit(), sort);
		Specification<Author> specification = this.getSearchSpecification(authorSearchDto);

		return authorRepository.findAll(specification, pageable).map(item -> modelMapper.map(item, AuthorDto.class));
	}

	private Specification<Author> getSearchSpecification(AuthorSearchDto authorSearchDto) {

		return new Specification<Author>() {
			@Override
			public Predicate toPredicate(Root<Author> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

				List<Predicate> predicates = new ArrayList<>();

				if (Strings.isNotBlank(authorSearchDto.getCode())) {
					predicates.add(criteriaBuilder.like(root.get("code"), "%" + authorSearchDto.getCode() + "%"));
				}

				if (Strings.isNotBlank(authorSearchDto.getName())) {
					predicates.add(criteriaBuilder.like(root.get("name"), "%" + authorSearchDto.getName() + "%"));
				}

				if (Strings.isNotBlank(authorSearchDto.getEmail())) {
					predicates.add(criteriaBuilder.like(root.get("email"), "%" + authorSearchDto.getEmail() + "%"));
				}

				if (Strings.isNotBlank(authorSearchDto.getPhoneNumber())) {
					predicates.add(criteriaBuilder.like(root.get("phoneNumber"), "%" + authorSearchDto.getPhoneNumber() + "%"));
				}

				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
}

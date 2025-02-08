package com.lms.service.imp;

import com.llq.springfilter.boot.Filter;
import com.lms.dto.CategoryDto;
import com.lms.dto.exception.LogicalException;
import com.lms.dto.exception.NotFoundException;
import com.lms.model.Book;
import com.lms.model.Category;
import com.lms.repository.BookRepository;
import com.lms.repository.CategoryRepository;
import com.lms.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final ModelMapper modelMapper;
    private final CategoryRepository repository;
    private final BookRepository bookRepository;

    @Override
    public CategoryDto save(@Validated CategoryDto dto) {
        Category entity = modelMapper.map(dto, Category.class);
        repository.save(entity);
        dto.setId(entity.getId());
        return dto;
    }

    @Override
    public List<CategoryDto> getAll() throws NotFoundException {
        List<Category> listCategory = repository.findAllByOrderByIdDesc();
        if(listCategory == null){
            return new ArrayList<>();
        }
        CategoryDto[] arrCategory = modelMapper.map(listCategory, CategoryDto[].class);
        return Arrays.asList(arrCategory);
    }

    @Override
    public CategoryDto update(Long id, CategoryDto dto) throws NotFoundException {
        Optional<Category> category = repository.findById(id);
        if (!category.isPresent())
            throw new NotFoundException("Không tìm thấy bản ghi với id: : " + id);

        Category entity = modelMapper.map(dto, Category.class);
        repository.save(entity);
        return dto;
    }

    @Override
    public CategoryDto getOne(Long id) throws NotFoundException {
        Optional<Category> entity = repository.findById(id);
        if (!entity.isPresent()) {
            throw new NotFoundException("Không tìm thấy bản ghi với id: : " + id);
        }
        CategoryDto dto = modelMapper.map(entity, CategoryDto.class);
        return dto;
    }

    @Override
    public Boolean delete(Long id) throws NotFoundException {
        Optional<Category> category = repository.findById(id);
        if (!category.isPresent())
            throw new NotFoundException("Không tìm thấy bản ghi với id: : " + id);
        List<Book> books = bookRepository.findByCategoryId(id);
        if(books.size() > 0){
            throw new LogicalException("Bản ghi đang có ràng buộc về dữ liệu!");
        }
        repository.deleteById(id);
        return true;
    }

    @Override
    public Boolean deleteList(CategoryDto[] dtos) throws NotFoundException {
        Category[] entities = modelMapper.map(dtos, Category[].class);
        for(int i = 0; i < dtos.length; i++){
            List<Book> books = bookRepository.findByCategoryId(dtos[i].getId());
            if(books.size() > 0){
                throw new LogicalException("Bản ghi đang có ràng buộc về dữ liệu!");
            }
        }
        repository.deleteAll(Arrays.asList(entities));
        return true;
    }

    public Page<CategoryDto> findByCondition(@Filter Specification<Category> spec, Pageable pageable) {
        Page<Category> entityPage = repository.findAll(spec, pageable);
        List<Category> entities = entityPage.getContent();
        return new PageImpl<>(modelMapper.map(entities, new TypeToken<List<CategoryDto>>() {}.getType()), pageable, entityPage.getTotalElements());
    }

    public List<Category> getTop5(){
        return repository.findFirst5ByOrderByNumberOfLoansDesc();
    }
}

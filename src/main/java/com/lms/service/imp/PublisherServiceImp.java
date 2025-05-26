package com.lms.service.imp;

import com.llq.springfilter.boot.Filter;
import com.lms.dto.*;
import com.lms.dto.PublisherDto;
import com.lms.dto.exception.LogicalException;
import com.lms.dto.exception.NotFoundException;
import com.lms.model.Author;
import com.lms.model.Book;
import com.lms.model.Publisher;
import com.lms.repository.BookRepository;
import com.lms.repository.PublisherRepository;
import com.lms.repository.UserRepository;
import com.lms.service.PublisherService;

import com.lms.util.Utils;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PublisherServiceImp implements PublisherService {
    private final ModelMapper modelMapper;
    private final PublisherRepository publisherRepository;
    private final BookRepository bookRepository;


    public PublisherDto save(PublisherDto PublisherDto) {
        Publisher publisherChecked = publisherRepository.findByEmail(PublisherDto.getEmail());
        if (publisherChecked != null) {
            throw new IllegalArgumentException("User email already exist");
        }
        Publisher publisher = modelMapper.map(PublisherDto, Publisher.class);
        publisherRepository.save(publisher);
        PublisherDto.setId(publisher.getId());
        return PublisherDto;
    }

    public List<PublisherDto> getAll() throws NotFoundException {
        List<Publisher> publishers = publisherRepository.findAll();
        if (publishers.size() < 1) {
            throw new NotFoundException("Publisher don't already exist");
        }
        PublisherDto[] publisherDtos = modelMapper.map(publishers, PublisherDto[].class);

        return Arrays.asList(publisherDtos);
    }

    public PublisherUpdateDto update(Long id, @Valid PublisherUpdateDto publisherUpdateDto) throws NotFoundException {
        Optional<Publisher> publisherOpt = publisherRepository.findById(id);
        if (!publisherOpt.isPresent()) {
            throw new NotFoundException("Không tìm thấy bản ghi với id: : " + id);
        }
        Publisher publisher = modelMapper.map(publisherUpdateDto, Publisher.class);
        publisher.setId(id);
        publisherRepository.save(publisher);
        publisherUpdateDto.setId(publisher.getId());
        return publisherUpdateDto;

    }

    public PublisherOneDto getOne(Long id) throws NotFoundException {

        Optional<Publisher> publisher = publisherRepository.findById(id);
        if (!publisher.isPresent()) {
            throw new NotFoundException("Không tìm thấy bản ghi với id: : " + id);
        }

        PublisherOneDto publisherOneDto = modelMapper.map(publisher.get(), PublisherOneDto.class);
        publisherOneDto.setId(id);
        publisherOneDto.getBooks().forEach(data -> {
            data.setPublisherId(id);
        });
        return publisherOneDto;

    }

    public Boolean delete(Long id) throws NotFoundException {
        Optional<Publisher> publisher = publisherRepository.findById(id);
        if (!publisher.isPresent()) {
            throw new NotFoundException("Không tìm thấy bản ghi với id: : " + id);
        }
        List<Book> books = bookRepository.findByPublisherId(id);
        if(books.size() > 0){
            throw new LogicalException("Bản ghi đang có ràng buộc về dữ liệu!");
        }
        publisherRepository.deleteById(id);
        return true;
    }

    public Boolean deleteList(PublisherDto[] dtos) throws NotFoundException {
        Publisher[] entities = modelMapper.map(dtos, Publisher[].class);
        for(int i = 0; i < dtos.length; i++){
            List<Book> books = bookRepository.findByPublisherId(dtos[i].getId());
            if(books.size() > 0){
                throw new LogicalException("Bản ghi đang có ràng buộc về dữ liệu!");
            }
        }
        publisherRepository.deleteAll(Arrays.asList(entities));
        return true;
    }

    public Page<PublisherDto> findByCondition(@Filter Specification<Publisher> spec, Pageable pageable) {
        Page<Publisher> entityPage = publisherRepository.findAll(spec, pageable);
        List<Publisher> entities = entityPage.getContent();
        return new PageImpl<>(modelMapper.map(entities, new TypeToken<List<PublisherDto>>() {}.getType()), pageable, entityPage.getTotalElements());
    }

    public Page<PublisherDto> search(PublisherSearchDto publisherSearchDto) {
        Sort sort = Utils.generatedSort(publisherSearchDto.getSort());
        Pageable pageable = PageRequest.of(publisherSearchDto.getPage(), publisherSearchDto.getLimit(), sort);
        Specification<Publisher> specification = this.getSearchSpecification(publisherSearchDto);

        return publisherRepository.findAll(specification, pageable).map(item -> modelMapper.map(item, PublisherDto.class));
    }

    private Specification<Publisher> getSearchSpecification(PublisherSearchDto publisherSearchDto) {

        return new Specification<Publisher>() {
            @Override
            public Predicate toPredicate(Root<Publisher> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                List<Predicate> predicates = new ArrayList<>();

                if (Strings.isNotBlank(publisherSearchDto.getCode())) {
                    predicates.add(criteriaBuilder.like(root.get("code"), "%" + publisherSearchDto.getCode() + "%"));
                }

                if (Strings.isNotBlank(publisherSearchDto.getName())) {
                    predicates.add(criteriaBuilder.like(root.get("name"), "%" + publisherSearchDto.getName() + "%"));
                }

                if (Strings.isNotBlank(publisherSearchDto.getEmail())) {
                    predicates.add(criteriaBuilder.like(root.get("email"), "%" + publisherSearchDto.getEmail() + "%"));
                }

                if (Strings.isNotBlank(publisherSearchDto.getPhoneNumber())) {
                    predicates.add(criteriaBuilder.like(root.get("phoneNumber"), "%" + publisherSearchDto.getPhoneNumber() + "%"));
                }

                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }
}

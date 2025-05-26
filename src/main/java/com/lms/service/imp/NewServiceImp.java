package com.lms.service.imp;

import com.llq.springfilter.boot.Filter;
import com.lms.dto.AuthorDto;
import com.lms.dto.AuthorSearchDto;
import com.lms.dto.NewDto;
import com.lms.dto.NewSearchDto;
import com.lms.dto.exception.NotFoundException;
import com.lms.model.Author;
import com.lms.model.NewEntity;
import com.lms.repository.NewRepository;
import com.lms.service.NewService;
import com.lms.util.TPage;
import com.lms.util.Utils;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class NewServiceImp implements NewService {
    private final ModelMapper modelMapper;
    private final NewRepository newRepository;

    public NewDto save(NewDto newDto) {
        NewEntity entity = modelMapper.map(newDto, NewEntity.class);
        newRepository.save(entity);
        newDto.setId(entity.getId());
        return newDto;
    }

    public List<NewDto> getAll() {
        List<NewEntity> listNewEntity = newRepository.findAllByOrderByIdDesc();
        if (listNewEntity == null) {
            return new ArrayList<>();
        }
        NewDto[] listNewDtos = modelMapper.map(listNewEntity, NewDto[].class);

        return Arrays.asList(listNewDtos);
    }

    public TPage<NewDto> getAllPageable(Pageable pageable) throws NotFoundException {
        try {
            Page<NewEntity> page = newRepository.findAll(PageRequest.of(pageable.getPageNumber(),
                    pageable.getPageSize(), Sort.by(Sort.Direction.ASC, "id")));
            // Page<New> page=newRepository.findAll(pageable);
            TPage<NewDto> tPage = new TPage<NewDto>();
            NewDto[] authorDtos = modelMapper.map(page.getContent(), NewDto[].class);

            tPage.setStat(page, Arrays.asList(authorDtos));
            return tPage;
        } catch (Exception e) {
            throw new NotFoundException("User email doesn't exist : " + e);
        }
    }

//	public List<NewDto> findAllByName(String name) throws NotFoundException {
//		List<New> authors = newRepository.findByNameOrSurname(name, name);
//		if (authors.size() < 1) {
//			throw new NotFoundException("New don't already exist");
//		}
//		NewDto[] authorDtos = modelMapper.map(authors, NewDto[].class);
//
//		return Arrays.asList(authorDtos);
//	}

    public NewDto update(Long id, @Valid NewDto newDto) throws NotFoundException {
        Optional<NewEntity> authorOpt = newRepository.findById(id);
        if (!authorOpt.isPresent()) {
            throw new NotFoundException("Không tìm thấy bản ghi với id: : " + id);
        }
        NewEntity entity = modelMapper.map(newDto, NewEntity.class);
        entity.setId(id);
        newRepository.save(entity);
        newDto.setId(entity.getId());
        return newDto;

    }

    public NewDto getOne(Long id) throws NotFoundException {

        Optional<NewEntity> entity = newRepository.findById(id);
        if (!entity.isPresent()) {
            throw new NotFoundException("Không tìm thấy bản ghi với id: : " + id);
        }

        NewDto newDto = modelMapper.map(entity.get(), NewDto.class);
        newDto.setId(id);
        return newDto;
    }

    public Boolean delete(Long id) throws NotFoundException {

        Optional<NewEntity> entity = newRepository.findById(id);
        if (!entity.isPresent()) {
            throw new NotFoundException("Không tìm thấy bản ghi với id: : " + id);
        }
        newRepository.deleteById(id);
        return true;
    }

    public Page<NewDto> findByCondition(@Filter Specification<NewEntity> spec, Pageable pageable) {
        Page<NewEntity> entityPage = newRepository.findAll(spec, pageable);
        List<NewEntity> entities = entityPage.getContent();
        return new PageImpl<>(modelMapper.map(entities, new TypeToken<List<NewDto>>() {
        }.getType()), pageable, entityPage.getTotalElements());
    }

    public Page<NewDto> search(NewSearchDto authorSearchDto) {
        Sort sort = Utils.generatedSort(authorSearchDto.getSort());
        Pageable pageable = PageRequest.of(authorSearchDto.getPage(), authorSearchDto.getLimit(), sort);
        Specification<NewEntity> specification = this.getSearchSpecification(authorSearchDto);

        return newRepository.findAll(specification, pageable).map(item -> modelMapper.map(item, NewDto.class));
    }

    private Specification<NewEntity> getSearchSpecification(NewSearchDto newSearchDto) {

        return new Specification<>() {
            @Override
            public Predicate toPredicate(Root<NewEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                List<Predicate> predicates = new ArrayList<>();

                if (Strings.isNotBlank(newSearchDto.getTitle())) {
                    predicates.add(criteriaBuilder.like(root.get("title"), "%" + newSearchDto.getTitle() + "%"));
                }

                if (Strings.isNotBlank(newSearchDto.getAuthorId())) {
                    predicates.add(criteriaBuilder.equal(root.get("authorId"), newSearchDto.getAuthorId()));
                }

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy, hh:mm:ss a");


                LocalDateTime from = (StringUtils.hasText(newSearchDto.getFromDate())) ? LocalDateTime.parse(newSearchDto.getFromDate(), formatter) : null;
                LocalDateTime to = (StringUtils.hasText(newSearchDto.getToDate())) ? LocalDateTime.parse(newSearchDto.getToDate(), formatter) : null;

                ZoneId zoneId = ZoneId.systemDefault(); // hoặc tùy theo múi giờ dữ liệu

                Date fromDate = (from != null) ? Date.from(from.atZone(zoneId).toInstant()) : null;
                Date toDate = (to != null) ? Date.from(to.atZone(zoneId).toInstant()) : null;

                if (from != null && to != null) {
                    predicates.add(criteriaBuilder.between(root.get("releaseDate"), fromDate, toDate));
                } else if (fromDate != null) {
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("releaseDate"), fromDate));
                } else if (toDate != null) {
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("releaseDate"), toDate));
                }

                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }
}

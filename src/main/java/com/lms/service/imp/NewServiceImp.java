package com.lms.service.imp;

import com.llq.springfilter.boot.Filter;
import com.lms.dto.NewDto;
import com.lms.dto.exception.NotFoundException;
import com.lms.model.NewEntity;
import com.lms.repository.NewRepository;
import com.lms.service.NewService;
import com.lms.util.TPage;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
		return new PageImpl<>(modelMapper.map(entities, new TypeToken<List<NewDto>>() {}.getType()), pageable, entityPage.getTotalElements());
	}
}

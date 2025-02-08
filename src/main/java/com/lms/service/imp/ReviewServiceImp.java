package com.lms.service.imp;

import com.lms.dto.ReviewDto;
import com.lms.dto.exception.NotFoundException;
import com.lms.model.LibraryUser;
import com.lms.model.Review;
import com.lms.repository.ReviewRepository;
import com.lms.repository.UserRepository;
import com.lms.service.ReviewService;
import com.lms.util.TPage;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
public class ReviewServiceImp implements ReviewService {
	private final ModelMapper modelMapper;
	private final ReviewRepository reviewRepository;
	private final UserRepository userRepository;

	public ReviewDto save(ReviewDto reviewDto) {
		Optional<LibraryUser> user = userRepository.findByUsername(reviewDto.getUsername());
		reviewDto.setUserId(user.get().getId());
		Review entity = modelMapper.map(reviewDto, Review.class);
		reviewRepository.save(entity);
		reviewDto.setId(entity.getId());
		return reviewDto;
	}

	public List<ReviewDto> getAll() {
		List<Review> listNewEntity = reviewRepository.findAllByOrderByIdDesc();
		if (listNewEntity == null) {
			return new ArrayList<>();
		}
		ReviewDto[] listNewDtos = modelMapper.map(listNewEntity, ReviewDto[].class);

		return Arrays.asList(listNewDtos);
	}

	public TPage<ReviewDto> getAllPageable(Pageable pageable) throws NotFoundException {
		try {
			Page<Review> page = reviewRepository.findAll(PageRequest.of(pageable.getPageNumber(),
					pageable.getPageSize(), Sort.by(Sort.Direction.ASC, "id")));
			// Page<New> page=reviewRepository.findAll(pageable);
			TPage<ReviewDto> tPage = new TPage<ReviewDto>();
			ReviewDto[] authorDtos = modelMapper.map(page.getContent(), ReviewDto[].class);

			tPage.setStat(page, Arrays.asList(authorDtos));
			return tPage;
		} catch (Exception e) {
			throw new NotFoundException("User email doesn't exist : " + e);
		}
	}

//	public List<ReviewDto> findAllByName(String name) throws NotFoundException {
//		List<New> authors = reviewRepository.findByNameOrSurname(name, name);
//		if (authors.size() < 1) {
//			throw new NotFoundException("New don't already exist");
//		}
//		ReviewDto[] authorDtos = modelMapper.map(authors, ReviewDto[].class);
//
//		return Arrays.asList(authorDtos);
//	}

	public ReviewDto update(Long id, @Valid ReviewDto reviewDto) throws NotFoundException {
		Optional<Review> authorOpt = reviewRepository.findById(id);
		if (!authorOpt.isPresent()) {
			throw new NotFoundException("Không tìm thấy bản ghi với id: : " + id);
		}
		Review entity = modelMapper.map(reviewDto, Review.class);
		entity.setId(id);
		reviewRepository.save(entity);
		reviewDto.setId(entity.getId());
		return reviewDto;

	}

	public ReviewDto getOne(Long id) throws NotFoundException {

		Optional<Review> entity = reviewRepository.findById(id);
		if (!entity.isPresent()) {
			throw new NotFoundException("Không tìm thấy bản ghi với id: : " + id);
		}

		ReviewDto reviewDto = modelMapper.map(entity.get(), ReviewDto.class);
		reviewDto.setId(id);
		return reviewDto;
	}

	public Boolean delete(Long id) throws NotFoundException {

		Optional<Review> entity = reviewRepository.findById(id);
		if (!entity.isPresent()) {
			throw new NotFoundException("Không tìm thấy bản ghi với id: : " + id);
		}
		reviewRepository.deleteById(id);
		return true;
	}

}

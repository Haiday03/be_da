package com.lms.service.imp;

import com.llq.springfilter.boot.Filter;
import com.lms.dto.*;
import com.lms.dto.exception.LogicalException;
import com.lms.dto.exception.NotEnoughQuantityException;
import com.lms.dto.exception.NotFoundException;
import com.lms.model.*;
import com.lms.repository.BookRepository;
import com.lms.repository.BorrowRepository;
import com.lms.repository.CategoryRepository;
import com.lms.repository.UserRepository;
import com.lms.service.BorrowService;
import com.lms.util.Utils;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class BorrowServiceImp implements BorrowService{
	private final ModelMapper modelMapper;
	private final BorrowRepository borrowRepository;
	private final UserRepository userRepository;
	private final BookRepository bookRepository;
	private final CategoryRepository categoryRepository;

	public BorrowDto save(BorrowDto borrowDto) {
		Optional<Book> book = bookRepository.findById(borrowDto.getBookId());
		Optional<Category> category = categoryRepository.findById(book.get().getCategoryId());
		checkQuantity(borrowDto.getQuantity(), book);

		Borrow borrow = modelMapper.map(borrowDto, Borrow.class);
		Optional<LibraryUser> user = userRepository.findByUsername(borrowDto.getUserName());
		borrow.setCreatedUser(user.get().getId());
		borrowRepository.save(borrow);

		// Cập nhật số lượng sách đang cho mượn
		int loaned = book.get().getLoaned();
		book.get().setLoaned((int) (borrowDto.getQuantity() + loaned));
		bookRepository.save(book.get());

		// Cập nhật số lượt sách đã cho mượn cho thể loại
		loaned = category.get().getNumberOfLoans();
		category.get().setNumberOfLoans((int) (borrowDto.getQuantity() + loaned));
		categoryRepository.save(category.get());

		borrowDto.setId(borrow.getId());
		return borrowDto;
	}

	private void checkQuantity(long quantity, Optional<Book> book){
		int exist = book.get().getQuantity() - book.get().getLoaned();
		if(quantity > exist)
			throw new NotEnoughQuantityException();
	}

	public List<BorrowDto> getAll() {
		List<Borrow> borrows = borrowRepository.findAll();
		if (borrows.size() < 1) {
			throw new NotFoundException("Borrow don't already exist");
		}
		BorrowDto[] borrowDtos = modelMapper.map(borrows, BorrowDto[].class);

		return Arrays.asList(borrowDtos);
	}

	public BorrowDto update(Long id, @Valid BorrowDto borrowDto) throws NotFoundException {
		Optional<Borrow> borrowOpt = borrowRepository.findById(id);
		if (!borrowOpt.isPresent()) {
			throw new NotFoundException("Không tồn tại bản ghi với id: " + id);
		}
		if(borrowDto.getRating() != 0 && borrowOpt.get().getRating() == 0){
			borrowDto.setReviewedDate(new Date());
			Optional<Book> book = bookRepository.findById(borrowDto.getBookId());
			Integer totalRatingObj = borrowRepository.getTotalRating(borrowOpt.get().getBookId());
			int totalRating = (totalRatingObj != null) ? totalRatingObj : 1;
			int numberOfRatings = borrowRepository.getNumberOfRatings(borrowOpt.get().getBookId());

			float rating = borrowDto.getRating();
			if(numberOfRatings > 0){
				rating = (float) (totalRating + borrowDto.getRating())/(numberOfRatings + 1);
			}
			book.get().setRating(rating);
			bookRepository.save(book.get());
		}
		Borrow borrow = modelMapper.map(borrowDto, Borrow.class);
		borrow.setId(id);
		borrowRepository.save(borrow);
		borrowDto.setId(borrow.getId());
		return borrowDto;

	}

	public BorrowDto getOne(Long id) throws NotFoundException {

		Optional<Borrow> borrow = borrowRepository.findById(id);
		if (!borrow.isPresent()) {
			throw new NotFoundException("Không tìm thấy bản ghi với id: : " + id);
		}

		BorrowDto borrowDto = modelMapper.map(borrow.get(), BorrowDto.class);
		borrowDto.setId(id);
		return borrowDto;

	}

	public List<Borrow> getReviewsByBook(Long bookId) throws NotFoundException {
		List<Borrow> borrowList = borrowRepository.findByBookIdAndRatingIsGreaterThan(bookId, 0);
		return borrowList;
	}

	public Boolean delete(Long id) throws NotFoundException {
		Optional<Borrow> borrow = borrowRepository.findById(id);
		if (!borrow.isPresent()) {
			throw new NotFoundException("Không tìm thấy bản ghi với id: : " + id);
		}
		if(borrow.get().getStatus() == 1){
			throw new LogicalException("Dữ liệu đang ở trạng thái mượn!");
		}
		borrowRepository.deleteById(id);
		return true;
	}

	public Boolean deleteList(long[] dtos) throws NotFoundException {
		List<Borrow> list = new ArrayList<>();
		for(int i = 0; i < dtos.length; i++){
			BorrowDto borrowDto = getOne(dtos[i]);
			if(borrowDto.getStatus() == 1){
				throw new LogicalException("Dữ liệu đang ở trạng thái mượn!");
			}
			Borrow borrow = modelMapper.map(borrowDto, Borrow.class);
			list.add(borrow);
		}

		borrowRepository.deleteAll(list);
		return true;
	}

	public Boolean deleteReview(Long id) throws NotFoundException {
		Optional<Borrow> borrow = borrowRepository.findById(id);
		if (!borrow.isPresent()) {
			throw new NotFoundException("Không tìm thấy bản ghi với id: : " + id);
		}
		borrow.get().setRating(0);
		borrow.get().setComment(null);
		borrow.get().setReviewedDate(null);
		borrowRepository.save(borrow.get());
		return true;
	}

	public Boolean deleteListReviews(long[] dtos) throws NotFoundException {
		List<Borrow> list = new ArrayList<>();
		for(int i = 0; i < dtos.length; i++){
			BorrowDto borrowDto = getOne(dtos[i]);
			Borrow borrow = modelMapper.map(borrowDto, Borrow.class);
			borrow.setRating(0);
			borrow.setComment(null);
			borrow.setReviewedDate(null);
			list.add(borrow);
		}

		borrowRepository.saveAll(list);
		return true;
	}

	public Page<BorrowDto> findByCondition(@Filter Specification<Borrow> spec, Pageable pageable) {
		Page<Borrow> entityPage = borrowRepository.findAll(spec, pageable);
		List<Borrow> entities = entityPage.getContent();
		return new PageImpl<>(modelMapper.map(entities, new TypeToken<List<BorrowDto>>() {}.getType()), pageable, entityPage.getTotalElements());
	}

	public BorrowDto updateBorrowStatus(Long id) throws Exception {
		Optional<Borrow> borrowOpt = borrowRepository.findById(id);
		if (!borrowOpt.isPresent()) {
			throw new NotFoundException("Không tồn tại bản ghi với id: " + id);
		}

		// Cập nhật trạng thái là đã trả sách
		borrowOpt.get().setStatus(2);
		borrowOpt.get().setReturnDate(new Date());
		borrowRepository.save(borrowOpt.get());

		// Cập nhật lại số lượng đã cho mượn của sách
		Optional<Book> book = bookRepository.findById(borrowOpt.get().getBookId());
		int quantityLent = book.get().getLoaned();
		long quantityReturned = borrowOpt.get().getQuantity();
		book.get().setLoaned((int) (quantityLent - quantityReturned));
		bookRepository.save(book.get());

		BorrowDto borrowDto = modelMapper.map(borrowOpt, BorrowDto.class);
		return borrowDto;
	}

	public Map<String, Object> getDashboardData(){
		Map<String, Object> result = new HashMap<>();
		List<String> labels = new ArrayList<>();
		List<Integer> borrowedDatas = new ArrayList<>();
		List<Integer> reviewedDatas = new ArrayList<>();

		for (int i = 30; i >= 0; i--){
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.set(calendar.DAY_OF_MONTH, calendar.get(calendar.DAY_OF_MONTH) - i);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			Date start = calendar.getTime();

			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			calendar.set(Calendar.MILLISECOND, 999);
			Date end = calendar.getTime();

			List<Borrow> borrowList = borrowRepository.findByCreatedDateBetween(start, end);
			List<Borrow> reviewList = borrowRepository.findByReviewedDateBetween(start, end);

			String label = start.getDate() + "/" + (start.getMonth() + 1) + "/" + String.valueOf(calendar.get(Calendar.YEAR)).substring(2, 4);
			labels.add(label);
			borrowedDatas.add(borrowList.size());
			reviewedDatas.add(reviewList.size());
		}

		result.put("labels", labels);
		result.put("borrowedDatas", borrowedDatas);
		result.put("reviewedDatas", reviewedDatas);

		return result;
	}

	public Page<BorrowDto> search(BorrowSearchDto authorSearchDto) {
		Sort sort = Utils.generatedSort(authorSearchDto.getSort());
		Pageable pageable = PageRequest.of(authorSearchDto.getPage(), authorSearchDto.getLimit(), sort);
		Specification<Borrow> specification = this.getSearchSpecification(authorSearchDto);

		return borrowRepository.findAll(specification, pageable).map(item -> modelMapper.map(item, BorrowDto.class));
	}

	private Specification<Borrow> getSearchSpecification(BorrowSearchDto newSearchDto) {

		return new Specification<>() {
			@Override
			public Predicate toPredicate(Root<Borrow> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

				List<Predicate> predicates = new ArrayList<>();

				Join<Borrow, LibraryUser> borrowerJoin = root.join("borrower", JoinType.INNER);
				if (Strings.isNotBlank(newSearchDto.getUsername())) {
					predicates.add(criteriaBuilder.like(borrowerJoin.get("username"), "%" + newSearchDto.getUsername() + "%"));
				}

				if (Strings.isNotBlank(newSearchDto.getStatus())) {
					predicates.add(criteriaBuilder.equal(root.get("status"), newSearchDto.getStatus()));
				}

				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

				LocalDate from = (StringUtils.hasText(newSearchDto.getFromDate()))
						? LocalDate.parse(newSearchDto.getFromDate(), formatter)
						: null;

				LocalDate to = (StringUtils.hasText(newSearchDto.getToDate()))
						? LocalDate.parse(newSearchDto.getToDate(), formatter)
						: null;

				ZoneId zoneId = ZoneId.systemDefault(); // Hoặc chọn ZoneId.of("Asia/Ho_Chi_Minh")

				Date fromDate = (from != null) ? Date.from(from.atStartOfDay(zoneId).toInstant()) : null;
				Date toDate = (to != null) ? Date.from(to.atStartOfDay(zoneId).toInstant()) : null;

				if (from != null && to != null) {
					predicates.add(criteriaBuilder.between(root.get("createdDate"), fromDate, toDate));
				} else if (fromDate != null) {
					predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdDate"), fromDate));
				} else if (toDate != null) {
					predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createdDate"), toDate));
				}

				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
}

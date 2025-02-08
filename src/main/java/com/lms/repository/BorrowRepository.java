package com.lms.repository;

import com.lms.model.Borrow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BorrowRepository extends JpaRepository<Borrow, Long>, JpaSpecificationExecutor<Borrow> {
    List<Borrow> findByCreatedDateBetween(Date start, Date end);
    List<Borrow> findByReviewedDateBetween(Date start, Date end);
    List<Borrow> findByBookIdAndRatingIsGreaterThan(Long bookId, Integer rating);

    @Query(value = "select sum(rating) from borrow where book_id = ?1 and rating > 0", nativeQuery = true)
    Integer getTotalRating(Long bookId);

    @Query(value = "select count(*) from borrow where book_id = ?1 and rating > 0", nativeQuery = true)
    Integer getNumberOfRatings(Long bookId);

    List<Borrow> findByBookId(Long id);
    List<Borrow> findByCreatedUser(Long id);
}

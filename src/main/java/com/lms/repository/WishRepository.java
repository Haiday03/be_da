package com.lms.repository;

import com.lms.model.Wish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishRepository extends JpaRepository<Wish, Long> {
    List<Wish> findByUserId(Long userId);
    Optional<Wish> findByUserIdAndBookId(Long userId, Long bookId);
}

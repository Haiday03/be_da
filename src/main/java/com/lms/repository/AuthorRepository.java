package com.lms.repository;

import com.lms.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long>, JpaSpecificationExecutor<Author> {
	Author findByEmail(String email);
	List<Author> findByCreatedDateBetween(Date startDate, Date endDate);
}

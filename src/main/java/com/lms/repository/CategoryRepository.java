package com.lms.repository;

import com.lms.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>, JpaSpecificationExecutor<Category> {
    List<Category> findAllByOrderByIdDesc();
    List<Category> findByCreatedDateBetween(Date startDate, Date endDate);
    List<Category> findFirst5ByOrderByNumberOfLoansDesc();
}

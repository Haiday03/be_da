package com.lms.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.lms.model.Category;
import com.lms.model.LibraryUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<LibraryUser, Long>, JpaSpecificationExecutor<LibraryUser> {

	List<LibraryUser> findByEmail(String email);

	Optional<LibraryUser> findByUsername(String username);

	Optional<LibraryUser> findByUserId(String userId);

	List<LibraryUser> findByCreatedDateBetween(Date startDate, Date endDate);

}

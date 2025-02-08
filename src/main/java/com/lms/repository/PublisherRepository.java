package com.lms.repository;

import com.lms.model.Author;
import com.lms.model.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface PublisherRepository  extends JpaRepository<Publisher, Long>, JpaSpecificationExecutor<Publisher> {
    Publisher findByEmail(String email);
}

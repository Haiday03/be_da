package com.lms.repository;

import com.lms.model.NewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewRepository extends JpaRepository<NewEntity, Long> , JpaSpecificationExecutor<NewEntity> {
    List<NewEntity> findAllByOrderByIdDesc();
}

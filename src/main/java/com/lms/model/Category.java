package com.lms.model;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "category")
@Data
@EntityListeners(AuditingEntityListener.class)
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @LastModifiedBy
    @Column(name = "modifier")
    private Long modifiedUser;

    @LastModifiedDate
    @Column(name = "last_modified")
    private Date modifiedDate;

    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private Long createdUser;

    @CreatedDate
    @Column(name = "created_date", updatable = false)
    private Date createdDate;

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "number_of_loans")
    private Integer numberOfLoans;
}

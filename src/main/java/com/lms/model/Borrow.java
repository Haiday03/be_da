package com.lms.model;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "borrow")
public class Borrow {
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

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "created_by", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
	private LibraryUser borrower;

	@CreatedDate
	@Column(name = "created_date", updatable = false)
	private Date createdDate;

	@Column(name = "book_id")
	private Long bookId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "book_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
	private Book book;

	@Column(name = "quantity")
	private Long quantity;

	@Column(name = "status")
	private Integer status;

	@Column(name = "returned_date")
	private Date returnDate;

	@Column(name = "rating")
	private Integer rating;

	@Column(name = "comment", columnDefinition = "TEXT")
	private String comment;

	@Column(name = "reviewed_date")
	private Date reviewedDate;

}

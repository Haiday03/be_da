package com.lms.model;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "book")
@EntityListeners(AuditingEntityListener.class)
public class Book {

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

	@Column(name = "name", length = 300)
	private String name;

	@Column(name = "code", length = 300)
	private String code;

	@Column(name = "description", length=99999)
	private String description;

	@Column(name = "price", length = 100)
	private int price;

	@Column(name = "quantity")
	private int quantity;

	@Column(name = "inventoryStatus", length = 100)
	private String inventoryStatus;

	@Column(name = "image", length = 9999)
	private String image;

	@Column(name = "rating", length = 100)
	private Float rating;

	@Column(name = "category_id")
	private Long categoryId;

	@Column(name = "author_id")
	private Long authorId;

	@Column(name = "publisher_id")
	private Long publisherId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "category_id", nullable = false, insertable = false, updatable = false)
	private Category category;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "author_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
	private Author author;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "publisher_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
	private Publisher publisher;

	@Column(name = "loaned")
	private int loaned;

	@Column(name = "publishing_year")
	private int publishingYear;

//	@OneToMany(mappedBy = "book",
//			cascade = CascadeType.ALL,
//			fetch = FetchType.LAZY)
//	private List<Comment> comment;

}

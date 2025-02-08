package com.lms.model;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Entity
@Table(name = "author")
public class Author {
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

	@NotNull
	@Column(name = "code", length = 6)
	private String code;

	@NotNull
	@Column(name = "name", length = 100)
	private String name;

	@NotNull
	@Column(name = "date_of_birth")
	private Date dateOfBirth;

	@Column(name = "nationality", length = 100)
	private String nationality;

	@Column(name = "address")
	private String address;

	@Column(name = "email", length = 100, unique = true)
	private String email;

	@Column(name = "phone_number", length = 15)
	private String phoneNumber;

	@Column(name = "description", length = 8000)
	private String description;

//	@OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//	private List<Book> books;
}

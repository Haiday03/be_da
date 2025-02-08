package com.lms.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
public class AuthorDto {

	private Long id;

	private Long modifiedUser;
	private Date modifiedDate;
	private Long createdUser;
	private Date createdDate;

	private String code;

	@NotNull
	private String name;

	private Date dateOfBirth;

	private String nationality;

	private String address;

	private String email;

	private String phoneNumber;

	private String description;

	private List<BookDtoForOneEntity> books;

}

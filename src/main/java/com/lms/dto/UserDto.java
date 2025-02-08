package com.lms.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import java.util.Date;

@Getter
@Setter
@Data
public class UserDto {

	private Long id;

	private Long modifiedUser;
	private Date modifiedDate;
	private Long createdUser;
	private Date createdDate;

	private String firstname;

	private String lastname;

	private String email;

	private String username;

	private String university;

	private String address;

	private String city;

	private String state;

	private String zip;

	private String about;

	private String facebook;

	private String instagram;

	private String twitter;

	private String cartId;

	private String phoneNumber;

	private String roleCode;

	UserDto(){
		roleCode = "USER";
	}

}

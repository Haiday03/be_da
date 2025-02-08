package com.lms.dto;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Data
public class UserDtoForOneEntity {

	private Long id;

	private String tcNo;

	@NotNull
	private String fullname;

	private String university;

	private String department;

	@NotNull
	private String email;

	@NotNull
	private String phone;

	private String address;


}

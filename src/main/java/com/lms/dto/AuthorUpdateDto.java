package com.lms.dto;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class AuthorUpdateDto {

	private Long id;

	@NotNull
	private String name;

	private String description;

	private String email;

}

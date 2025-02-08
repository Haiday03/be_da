package com.lms.dto;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Data
@NoArgsConstructor
public class AuthorOneDto {

	private Long id;

	@NotNull
	private String name;

	private String code;

	@NotNull
	private String surname;

	private String description;

	private String email;

}

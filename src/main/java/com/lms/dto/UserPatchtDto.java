package com.lms.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Data
public class UserPatchtDto {

	private Long id;

	private Long UserId;

	private Long bookId;

}

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
public class BookDtoForOneEntity {

	private Long id;

	@NotNull
	private String name;

	@NotNull
	private String code;

	private String description;

	private int price;

	private int quantity;

	private String inventoryStatus;

	private String image;

	private int rating;

	private String category;

	@NotNull
	private Long authorId;
	@NotNull
	private Long publisherId;
}

package com.lms.dto;

import javax.validation.constraints.NotNull;

import com.lms.model.Comment;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Data
public class BookUpdateDto {

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

	private List<Comment> commentList;


}

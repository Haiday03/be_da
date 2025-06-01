package com.lms.dto;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import com.lms.model.Author;
import com.lms.model.Category;
import com.lms.model.Comment;
import com.lms.model.Publisher;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Data
public class BookOneDto {

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

	private int loaned;

	@NotNull
	private Long categoryId;
	@NotNull
	private Long authorId;
	@NotNull
	private Long publisherId;

	private Category category;
	private Author author;
	private Publisher publisher;

	private List<Comment> commentList;

}

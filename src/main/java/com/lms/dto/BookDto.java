package com.lms.dto;

import com.lms.model.Author;
import com.lms.model.Category;
import com.lms.model.LibraryUser;
import com.lms.model.Publisher;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class BookDto {

	private Long id;

	private Long modifiedUser;
	private Date modifiedDate;
	private Long createdUser;
	private Date createdDate;

	@NotNull
	private String name;

	@NotNull
	private String code;

	private String description;

	private int price;

	private int quantity;

	private String inventoryStatus;

	private String image;

	private Float rating;

	@NotNull
	private Long categoryId;
	@NotNull
	private Long authorId;
	@NotNull
	private Long publisherId;

	private Category category;
	private Author author;
	private Publisher publisher;

	private int loaned;

	private int publishingYear;

	public BookDto(){
		this.loaned = 0;
	}
}

package com.lms.dto;

import com.lms.model.Book;
import com.lms.model.LibraryUser;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class ReviewDto {
	private Long id;
	private Long bookId;
	private Book book;
	private Long userId;
	private LibraryUser user;
	private Integer point;
	private String content;
	private Date reviewedDate;
	private String username;
}

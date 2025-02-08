package com.lms.dto;

import com.lms.model.Book;
import com.lms.model.LibraryUser;
import lombok.Data;

import java.util.Date;

@Data
public class BorrowDto {
	private Long id;
	private Long modifiedUser;
	private Date modifiedDate;
	private Long createdUser;
	private LibraryUser borrower;
	private Date createdDate;
	private Long bookId;
	private Book book;
	private Long quantity;
	private Integer status;
	private Date returnDate;
	private String userName;
	private Integer rating;
	private String comment;
	private Date reviewedDate;

	BorrowDto(){
		this.rating = 0;
	}
}

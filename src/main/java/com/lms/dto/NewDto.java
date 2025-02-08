package com.lms.dto;

import com.lms.model.Author;
import lombok.Data;

import java.util.Date;

@Data
public class NewDto {
	private Long id;
	private String code;
	private String title;
	private Long authorId;
	private Author author;
	private Date releaseDate;
	private String keyword;
	private String summary;
	private String content;
	private String thumnail;
}

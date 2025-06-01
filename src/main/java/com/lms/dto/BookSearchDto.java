package com.lms.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BookSearchDto {

    private String code;
    private String name;
    private String categoryId;
    private String publisherId;
    private String publishingYear;
    private String currentBookId;
    private Integer page;
    private Integer limit;
    private String sort;
}

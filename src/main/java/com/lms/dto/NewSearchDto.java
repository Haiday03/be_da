package com.lms.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewSearchDto {

    private String title;
    private String authorId;
    private String fromDate;
    private String toDate;
    private Integer page;
    private Integer limit;
    private String sort;
}

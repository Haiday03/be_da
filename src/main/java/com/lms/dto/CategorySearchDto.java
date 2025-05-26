package com.lms.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategorySearchDto {

    private String code;
    private String name;
    private Integer page;
    private Integer limit;
    private String sort;
}

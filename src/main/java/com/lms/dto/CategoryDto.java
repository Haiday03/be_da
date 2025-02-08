package com.lms.dto;

import lombok.Data;

import java.util.Date;

@Data
public class CategoryDto {
    private Long id;
    private Long modifiedUser;
    private Long createdUser;
    private Date modifiedDate;
    private Date createdDate;
    private String code;
    private String name;
    private String description;
    private Integer numberOfLoans;

    CategoryDto(){
        numberOfLoans = 0;
    }
}

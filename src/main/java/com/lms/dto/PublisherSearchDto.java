package com.lms.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PublisherSearchDto {

    private String code;
    private String name;
    private String email;
    private String phoneNumber;
    private Integer page;
    private Integer limit;
    private String sort;
}

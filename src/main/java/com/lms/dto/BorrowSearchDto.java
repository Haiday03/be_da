package com.lms.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BorrowSearchDto {

    private String username;
    private String status;
    private String fromDate;
    private String toDate;
    private Integer page;
    private Integer limit;
    private String sort;
}

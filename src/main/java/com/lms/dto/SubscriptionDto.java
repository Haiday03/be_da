package com.lms.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
public class SubscriptionDto {
    private String name;
    private Date dateCreated;
    private String type;
}

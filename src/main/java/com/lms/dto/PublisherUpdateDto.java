package com.lms.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Data
public class PublisherUpdateDto {
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String surname;

    private String description;

    private String email;

}

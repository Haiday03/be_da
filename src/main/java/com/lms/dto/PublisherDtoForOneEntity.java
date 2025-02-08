package com.lms.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Data
@NoArgsConstructor
public class PublisherDtoForOneEntity {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String surname;

    private String email;

    private String description;
}

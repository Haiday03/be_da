package com.lms.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@Data
public class PublisherDto {
    private Long id;

    private String code;

    @NotNull
    private String name;

    private String email;

    private String address;

    private String phoneNumber;

    private String linkWebsite;

    private List<BookDtoForOneEntity> books;

}

package com.lms.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@Data
@NoArgsConstructor
public class PublisherOneDto {
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String surname;

    private String description;

    private String email;

    private List<BookOneDto> books;
}

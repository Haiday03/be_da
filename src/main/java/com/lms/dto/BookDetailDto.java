package com.lms.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@Data
public class BookDetailDto {
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String code;

    private String description;

    private int price;

    private int quantity;

    private String inventoryStatus;

    private String image;

    private int rating;

    private String category;

    private String authorName;

    private String publisherName;
}

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
public class BookForDashboard {

    private Long id;

    @NotNull
    private String name;

    private int price;

    private String inventoryStatus;

    private String image;

}

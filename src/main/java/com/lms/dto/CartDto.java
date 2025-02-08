package com.lms.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Data
public class CartDto {

    private Long id;

    private List<BookOneDto> bookList;

    private double price;

    private Boolean payed = false;

    public void setPrice(double price) {
        this.price += price;
    }

    private LocalDateTime dateCreated;
}

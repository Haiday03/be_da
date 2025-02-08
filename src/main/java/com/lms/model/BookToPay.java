package com.lms.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookToPay {

    private long bookId;

    private int quantity;

    private double price;

}

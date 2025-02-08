package com.lms.dto.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
public class BookAlreadyInCartException extends RuntimeException {

    public BookAlreadyInCartException() {
        super(String.format("Sách này đã tồn tại trong yêu thích!"));
    }

}

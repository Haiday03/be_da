package com.lms.dto.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
public class NotEnoughQuantityException extends RuntimeException {

    public NotEnoughQuantityException() {
        super(String.format("Số lượng sách còn lại không đủ để đặt mượn!"));
    }

}

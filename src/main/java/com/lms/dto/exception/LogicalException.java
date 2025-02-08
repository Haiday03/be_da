package com.lms.dto.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
public class LogicalException extends RuntimeException {

    public LogicalException(String message) {
        super(String.format(message));
    }

}

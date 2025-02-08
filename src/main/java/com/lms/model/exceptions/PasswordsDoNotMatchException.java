package com.lms.model.exceptions;

public class PasswordsDoNotMatchException extends RuntimeException{

    public PasswordsDoNotMatchException() {
        super("Mật khẩu không chính xác!");
    }
}

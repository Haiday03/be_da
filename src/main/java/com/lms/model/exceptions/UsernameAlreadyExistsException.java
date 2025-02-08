package com.lms.model.exceptions;

public class UsernameAlreadyExistsException extends RuntimeException{
    public UsernameAlreadyExistsException(String username) {
        super(String.format("Không tồn tại người dùng với tên đăng nhập: %s", username));
    }
}

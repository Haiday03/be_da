package com.lms.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteBookFromWish {
    private String userId;
    private Long wishId;

    public DeleteBookFromWish(String userId, Long wishId) {
        this.userId = userId;
        this.wishId = wishId;
    }
}

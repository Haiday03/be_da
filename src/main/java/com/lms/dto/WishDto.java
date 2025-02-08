package com.lms.dto;

import com.lms.model.Book;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
public class WishDto {
    private Long id;
    private Long userId;
    private Long bookId;
    private Book book;
    private LocalDateTime dateCreated;
}

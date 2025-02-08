package com.lms.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Data
public class CommentDto {
    private Long id;
    private String name;
    private String content;
    private String userId;
    private String username;
    private Date dateCreated;
    private int likes;
    private int dislikes;
    private long bookId;
}

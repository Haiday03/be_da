package com.lms.service;

import com.lms.dto.BookOneDto;
import com.lms.dto.CommentDto;
import com.lms.dto.exception.NotFoundException;


public interface CommentService {

    public Integer deleteComment(Long commentId,String userId);
    public CommentDto[] getComments(Long bookId);
    public CommentDto comment(CommentDto commentDto, BookOneDto bookOneDto) throws NotFoundException;
}

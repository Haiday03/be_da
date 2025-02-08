package com.lms.api;

import com.lms.dto.BookOneDto;
import com.lms.dto.CommentDto;
import com.lms.dto.exception.BookNotFoundException;
import com.lms.model.DecodedToken;
import com.lms.service.imp.BookServiceImp;
import com.lms.service.imp.CommentServiceImp;
import com.lms.util.ApiPaths;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(ApiPaths.CommentCtrl.CTRL)
@CrossOrigin
public class CommentRestController {

    public final CommentServiceImp commentServiceImp;
    public final BookServiceImp bookServiceImp;

    private final ModelMapper modelMapper;

    public CommentRestController(CommentServiceImp commentServiceImp, BookServiceImp bookServiceImp, ModelMapper modelMapper) {
        this.commentServiceImp = commentServiceImp;
        this.bookServiceImp = bookServiceImp;
        this.modelMapper = modelMapper;
    }

    @GetMapping("{bookId}")
    public ResponseEntity<CommentDto[]> getComments(@PathVariable Long bookId) {

        return ResponseEntity.ok(this.commentServiceImp.getComments(bookId));
    }

    @PostMapping()
    public ResponseEntity<CommentDto> comment(@RequestBody CommentDto comment) throws Exception {
        BookOneDto bookOneDto=this.bookServiceImp.getOne(comment.getBookId()).orElseThrow(()->new BookNotFoundException(comment.getBookId()));
        return ResponseEntity.ok(this.commentServiceImp.comment(comment,bookOneDto));

    }
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Integer> delete(@PathVariable(required = true) Long commentId, HttpServletRequest httpServletRequest) throws Exception {
        String token=httpServletRequest.getHeader("Authorization");
        DecodedToken decoded = DecodedToken.getDecoded(token);

        return ResponseEntity.ok(this.commentServiceImp.deleteComment(commentId,decoded.username));
    }
}

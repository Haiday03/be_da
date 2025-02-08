package com.lms.service.imp;

import com.lms.dto.BookOneDto;
import com.lms.dto.CommentDto;
import com.lms.dto.exception.NotFoundException;
import com.lms.model.Author;
import com.lms.model.Book;
import com.lms.model.Comment;
import com.lms.model.Publisher;
import com.lms.repository.*;
import com.lms.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImp implements CommentService {
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final AuthorRepository authorRepository;

    private final CommentRepository commentRepository;
    private final PublisherRepository publisherRepository;
    private final BookRepository bookRepository;

    public CommentServiceImp(ModelMapper modelMapper, UserRepository userRepository, AuthorRepository authorRepository, CommentRepository commentRepository, PublisherRepository publisherRepository, BookRepository bookRepository) {
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.authorRepository = authorRepository;
        this.commentRepository = commentRepository;
        this.publisherRepository = publisherRepository;
        this.bookRepository = bookRepository;
    }


    @Override
    public Integer deleteComment(Long commentId, String username) {

        return this.commentRepository.deleteCommentByIdAndAndUsername(commentId,username);
    }

    public CommentDto[] getComments(Long bookId){
       List<Comment> commentList= commentRepository.getCommentByBookId(bookId);
       CommentDto[] commentDtos = modelMapper.map(commentList,CommentDto[].class);
       return commentDtos;
    }

    @Override
    public CommentDto comment(CommentDto commentDto, BookOneDto bookOneDto) throws NotFoundException {
        Optional<Book> bookOpt = bookRepository.findById(commentDto.getBookId());
        if (!bookOpt.isPresent()) {
            throw new NotFoundException("Book does not exist id : " + commentDto.getBookId());
        }
        Optional<Author> author = authorRepository.findById(bookOneDto.getAuthorId());
        if (!author.isPresent()) {
            throw new NotFoundException("Author does not exist id : " + bookOneDto.getAuthorId());
        }
        Book book = modelMapper.map(bookOneDto, Book.class);
//        Optional<Author> authorOpt = authorRepository.findById(bookOneDto.getAuthorId());
//        book.setAuthor(authorOpt.get());
//        Optional<Publisher> publisherOpt = publisherRepository.findById(bookOneDto.getPublisherId());
//        book.setPublisher(publisherOpt.get());

        Comment comment= modelMapper.map(commentDto,Comment.class);
        comment.setBook(this.bookRepository.getOne(commentDto.getBookId()));

//        if(book.getComment()==null || book.getComment().size()<1){
//            List<Comment> commentList = new ArrayList<>();
//            commentList.add(comment);
//        }
//        else
//        {
//            book.getComment().add(comment);
//        }

        this.commentRepository.save(comment);
        return commentDto;
    }
}

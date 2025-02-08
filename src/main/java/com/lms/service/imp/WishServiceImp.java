package com.lms.service.imp;

import com.lms.dto.DeleteBookFromWish;
import com.lms.dto.WishDto;
import com.lms.dto.exception.BookAlreadyInCartException;
import com.lms.dto.exception.NotFoundException;
import com.lms.dto.exception.UserNotFoundException;
import com.lms.model.Category;
import com.lms.model.LibraryUser;
import com.lms.model.Wish;
import com.lms.repository.BookRepository;
import com.lms.repository.CartRepository;
import com.lms.repository.UserRepository;
import com.lms.repository.WishRepository;
import com.lms.service.BookService;
import com.lms.service.WishService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class WishServiceImp implements WishService {
    private final ModelMapper modelMapper;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    private final WishRepository wishRepository;
    private final BookRepository bookRepository;
    private final BookService bookService;

    public WishServiceImp(ModelMapper modelMapper, CartRepository cartRepository, UserRepository userRepository, WishRepository wishRepository, BookRepository bookRepository, BookService bookService) {
        this.modelMapper = modelMapper;
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.wishRepository = wishRepository;
        this.bookRepository = bookRepository;
        this.bookService = bookService;
    }

    @Override
    public List<WishDto> getActiveWish(String username) {
        LibraryUser user = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

//        Wish wish = this.wishRepository
//                .findByLibraryUser(user)
//                .orElseGet(() -> {
//                    Wish newWish = new Wish(user);
//                    return this.wishRepository.save(newWish);
//                });
//
//        WishDto wishDto = modelMapper.map(wish, WishDto.class);

        List<Wish> listWish = wishRepository.findByUserId(user.getId());

        WishDto[] arrWish = modelMapper.map(listWish, WishDto[].class);
        return Arrays.asList(arrWish);
    }

    @Override
    public boolean deleteBookFromWish(DeleteBookFromWish deleteBook) {
        Optional<Wish> wish = wishRepository.findById(deleteBook.getWishId());
        if (!wish.isPresent())
            throw new NotFoundException("Không tìm thấy bản ghi với id: " + deleteBook.getWishId());
        wishRepository.deleteById(deleteBook.getWishId());
        return true;
    }

//    @Override
//    public Wish getWish(String userId) {
//        LibraryUser user = this.userRepository.findByUserId(userId)
//                .orElseThrow(() -> new UserNotFoundException(userId));
//
//        return this.wishRepository
//                .findByLibraryUser(user)
//                .orElseGet(() -> {
//                    Wish wish = new Wish(user);
//                    return this.wishRepository.save(wish);
//                });
//    }

    @Override
    public WishDto addBookToWish(String username, Long bookId) {
//        WishDto wishDto = this.getActiveWish(userId);
//
//        BookOneDto book = this.bookService.getOne(bookId)
//                .orElseThrow(() -> new BookNotFoundException(bookId));
//
//        if(wishDto.getBookList()
//                .stream().filter(i -> i.getId().equals(bookId))
//                .collect(Collectors.toList()).size() > 0)
//            throw new BookAlreadyInCartException(bookId, userId);
//
//
//        wishDto.getBookList().add(book);
//
//        Wish wish = modelMapper.map(wishDto,Wish.class);
//        wish.setLibraryUser(this.userRepository.findByUserId(userId).orElseThrow(()-> new UserNotFoundException(userId)));
//        this.wishRepository.save(wish);
//
//        return wishDto;

        LibraryUser user = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        // Check tồn tại
        exist(user.getId(), bookId);

        Wish wish = new Wish();
        wish.setBookId(bookId);
        wish.setUserId(user.getId());

        return modelMapper.map(wishRepository.save(wish), WishDto.class);
    }

    private void exist(Long userId, Long bookId){
        Optional<Wish> wish = wishRepository.findByUserIdAndBookId(userId, bookId);
        if(wish.isPresent()){
            throw new BookAlreadyInCartException();
        }
    }

    @Override
    public Wish save(Wish wish) {
        return this.wishRepository.save(wish);
    }
}

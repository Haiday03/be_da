package com.lms.service.imp;

import com.lms.dto.BookOneDto;
import com.lms.dto.CartDto;
import com.lms.dto.DeleteBookFromCartModel;
import com.lms.dto.exception.BookAlreadyInCartException;
import com.lms.dto.exception.BookNotFoundException;
import com.lms.dto.exception.CartNotFoundException;
import com.lms.dto.exception.UserNotFoundException;
import com.lms.model.Book;
import com.lms.model.Cart;
import com.lms.model.LibraryUser;
import com.lms.repository.BookRepository;
import com.lms.repository.CartRepository;
import com.lms.repository.UserRepository;
import com.lms.service.BookService;
import com.lms.service.CartService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartServiceImp implements CartService {

    private final ModelMapper modelMapper;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    private final BookRepository bookRepository;
    private final BookService bookService;

    public CartServiceImp(ModelMapper modelMapper, CartRepository cartRepository, UserRepository userRepository, BookRepository bookRepository, BookService bookService) {
        this.modelMapper = modelMapper;
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.bookService = bookService;
    }


    @Override
    public List<BookOneDto> listAllProductsInShoppingCart(Long cartId) {
        if(!this.cartRepository.findById(cartId).isPresent())
            throw new CartNotFoundException(cartId);

            List<Book> books=this.cartRepository.findById(cartId).get().getBookList();

            List<BookOneDto> bookOneDto= (List<BookOneDto>) modelMapper.map(books,BookOneDto.class);

        return bookOneDto;
    }


    @Override
    public CartDto getActiveShoppingCart(String userId) {
        LibraryUser user = this.userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        Cart cart = this.cartRepository
                .findByLibraryUser(user)
                .orElseGet(() -> {
                        Cart newcart = new Cart(user);
                        return this.cartRepository.save(newcart);
                        });

        CartDto cartDto = modelMapper.map(cart, CartDto.class);
        cartDto.setPrice(cartDto.getBookList().stream().mapToDouble(o->o.getPrice()).sum());
        return cartDto;

    }
    @Override
    public Cart getCart(String userId) {
        LibraryUser user = this.userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        return this.cartRepository
                .findByLibraryUser(user)
                .orElseGet(() -> {
                    Cart newcart = new Cart(user);
                    return this.cartRepository.save(newcart);
                });
    }

    @Override
    public CartDto addProductToShoppingCart(String userId, Long bookId) {
        CartDto cartDto = this.getActiveShoppingCart(userId);

        BookOneDto book = this.bookService.getOne(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId));

        if(cartDto.getBookList()
                .stream().filter(i -> i.getId().equals(bookId))
                .collect(Collectors.toList()).size() > 0)
            throw new BookAlreadyInCartException();


        cartDto.getBookList().add(book);

        Cart cart = modelMapper.map(cartDto,Cart.class);
        cart.setLibraryUser(this.userRepository.findByUserId(userId).orElseThrow(()-> new UserNotFoundException(userId)));
        this.cartRepository.save(cart);
        return cartDto;
    }

    @Override
    public Cart save(Cart cart) {
        return this.cartRepository.save(cart);
    }

    public boolean deleteBookFromShoppingCart(DeleteBookFromCartModel deleteBook)
    {
//        if(deleteBook!=null){
//            LibraryUser user= this.userRepository.findByUserId(deleteBook.getUserId()).orElseThrow(() -> new UserNotFoundException(deleteBook.getUserId()));
//            user.getCarts().stream().filter(f->f.getId()==deleteBook.getCartId()).findFirst().get().getBookList().removeIf(book -> book.getId()== deleteBook.getBookId());
//            this.userRepository.save(user);
//            return true;
//        }
//        return false;
        return true;
    }
}

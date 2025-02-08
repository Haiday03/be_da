package com.lms.service;


import com.lms.dto.BookOneDto;
import com.lms.dto.CartDto;
import com.lms.dto.DeleteBookFromCartModel;
import com.lms.model.Cart;

import java.util.List;


public interface CartService {

    List<BookOneDto> listAllProductsInShoppingCart(Long cartId);
    CartDto getActiveShoppingCart(String userId);

    boolean deleteBookFromShoppingCart(DeleteBookFromCartModel deleteBook);
    Cart getCart(String userId);
    CartDto addProductToShoppingCart(String userId, Long bookId);

    Cart save(Cart cart);
}

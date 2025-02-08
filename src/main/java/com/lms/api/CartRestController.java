package com.lms.api;

import com.lms.dto.CartDto;
import com.lms.dto.DeleteBookFromCartModel;
import com.lms.model.Cart;
import com.lms.model.DecodedToken;
import com.lms.model.enumerations.CartStatus;
import com.lms.service.imp.CartServiceImp;
import com.lms.service.imp.UserServiceImp;
import com.lms.util.ApiPaths;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Collections;

@RestController
@RequestMapping(ApiPaths.CartCtrl.CTRL)
@CrossOrigin
public class CartRestController {

    private final CartServiceImp cartServiceImp;
    private final UserServiceImp userServiceImp;

    public CartRestController(CartServiceImp cartServiceImp, UserServiceImp userServiceImp) {
        this.cartServiceImp = cartServiceImp;
        this.userServiceImp = userServiceImp;
    }

    @GetMapping()
    public ResponseEntity<CartDto> getCart(HttpServletRequest httpServletRequest) throws UnsupportedEncodingException {
        String token=httpServletRequest.getHeader("Authorization");
        DecodedToken decoded = DecodedToken.getDecoded(token);
        return ResponseEntity.ok(this.cartServiceImp.getActiveShoppingCart(decoded.userId));
    }

    @PostMapping("/finished")
    public ResponseEntity<Boolean> setFinished(HttpServletRequest httpServletRequest) throws UnsupportedEncodingException {
        String token=httpServletRequest.getHeader("Authorization");
        DecodedToken decoded = DecodedToken.getDecoded(token);

       Cart cart= this.cartServiceImp.getCart(decoded.userId);
       cart.setBookList(Collections.emptyList());
       cart.setCartStatus(CartStatus.CREATED);
       cart.setPaid(false);
       this.cartServiceImp.save(cart);
       return ResponseEntity.ok(true);
    }

    @PostMapping("/add-book/{bookId}")
    public ResponseEntity<CartDto> addBookToCart(HttpServletRequest httpServletRequest,
                                              @PathVariable Long bookId) throws UnsupportedEncodingException {

        String token=httpServletRequest.getHeader("Authorization");
        DecodedToken decoded = DecodedToken.getDecoded(token);

       if(bookId !=null)
       {
           return ResponseEntity.ok(cartServiceImp.addProductToShoppingCart(decoded.userId, bookId));
       }
       return ResponseEntity.badRequest().build();
    }

//    @DeleteMapping("/delete-book/{cartId}/{bookId}")
//    public ResponseEntity<Boolean> deleteBookFromCart(HttpServletRequest httpServletRequest,
//                                                      @PathVariable(name = "cartId", required = true) Long cartId,
//                                                      @PathVariable(name = "bookId", required = true) Long bookId) throws UnsupportedEncodingException {
//        String token=httpServletRequest.getHeader("Authorization");
//        DecodedToken decoded = DecodedToken.getDecoded(token);
//
//        if(bookId !=null) {
//            DeleteBookFromCartModel deleteBook = new DeleteBookFromCartModel(decoded.userId,cartId,bookId);
//            return ResponseEntity.ok(cartServiceImp.deleteBookFromShoppingCart(deleteBook));
//        }
//        return ResponseEntity.ok(true);
//    }

}

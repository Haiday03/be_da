package com.lms.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteBookFromCartModel {
   private String userId;
   private Long cartId;
   private Long bookId;

   public DeleteBookFromCartModel(String userId, Long cartId, Long bookId) {
      this.userId = userId;
      this.cartId = cartId;
      this.bookId = bookId;
   }

}

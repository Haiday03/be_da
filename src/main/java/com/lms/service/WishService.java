package com.lms.service;

import com.lms.dto.DeleteBookFromWish;
import com.lms.dto.WishDto;
import com.lms.model.Wish;

import java.util.List;

public interface WishService {

    List<WishDto> getActiveWish(String userId);

    boolean deleteBookFromWish(DeleteBookFromWish deleteBook);

//    Wish getWish(String userId);

    WishDto addBookToWish(String userId, Long bookId);

    Wish save(Wish wish);
}

package com.lms.service;

        import com.lms.dto.DeleteBookFromWish;
        import com.lms.dto.WishDto;
        import com.lms.model.Wish;

        import java.util.List;

public interface WishService {

    List<WishDto> getActiveWish(String userId);

    boolean deleteBookFromWish(DeleteBookFromWish deleteBook);

//    Wish getWish(String userId);

    WishDto addBookToWish(String userId, Long bookId);

    Wish save(Wish wish);
}


package com.lms.service;

        import com.lms.dto.DeleteBookFromWish;
        import com.lms.dto.WishDto;
        import com.lms.model.Wish;

        import java.util.List;

public interface WishService {

    List<WishDto> getActiveWish(String userId);

    boolean deleteBookFromWish(DeleteBookFromWish deleteBook);

//    Wish getWish(String userId);

    WishDto addBookToWish(String userId, Long bookId);

    Wish save(Wish wish);
}

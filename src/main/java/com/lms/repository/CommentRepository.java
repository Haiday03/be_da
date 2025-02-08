package com.lms.repository;

import com.lms.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {

    @Query(value = "SELECT * FROM public.comment as co WHERE co.book_id=:bookId ORDER BY date_created desc",nativeQuery = true)
    List<Comment> getCommentByBookId(@Param("bookId") long bookId);
    @Modifying
    @Query(value = "DELETE FROM public.comment cm WHERE cm.id=:commentId AND cm.username =:username",nativeQuery = true)
    Integer deleteCommentByIdAndAndUsername(@Param("commentId") Long commentId,@Param("username") String userId);
}

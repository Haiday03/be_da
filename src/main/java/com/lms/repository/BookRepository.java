package com.lms.repository;

import com.lms.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {

	@Query(value = "SELECT * FROM BOOK b WHERE b.code LIKE %:code% AND b.name LIKE %:name%", nativeQuery = true)
	List<Book> findByCondition(@Param("code") String code, @Param("name") String name);

	List<Book> findByName(String name);

	@Query("select b from Book b where b.name like %:name%")
	List<Book> SearchBooksByName(String name);

	@Query(value="SELECT b.* FROM Book b INNER JOIN checkout_books cb ON cb.books_id=b.id GROUP BY b.id ORDER BY COUNT(cb.books_id) DESC LIMIT 10",nativeQuery = true)
	public List<Book> getRecentSales();

	@Query(value=
			"SELECT DISTINCT b.* " +
			"FROM Book b " +
			"INNER JOIN public.comment com ON b.id = com.book_id " +
			"INNER JOIN public.wish_book_list wb ON b.id = wb.book_list_id " +
			"INNER JOIN public.checkout_books cb ON b.id = cb.books_id " +
			"WHERE b.id != :id AND b.rating >= 4 AND b.category_id = :categoryId",nativeQuery = true)
	public List<Book> getRecommended(Long categoryId,Long id);
	List<Book> findByCreatedDateBetween(Date startDate, Date endDate);

	List<Book> findByAuthorId(Long id);
	List<Book> findByCategoryId(Long id);
	List<Book> findByPublisherId(Long id);
}

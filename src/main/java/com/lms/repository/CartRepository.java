package com.lms.repository;

import com.lms.model.Cart;
import com.lms.model.LibraryUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByLibraryUser(LibraryUser libraryUser);
}

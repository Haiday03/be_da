package com.lms.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.lms.model.enumerations.CartStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Entity
@Table(name = "CART")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Getter
@Setter
@NoArgsConstructor
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private LibraryUser libraryUser;

    @ManyToMany
    @Column(name = "BOOK_LIST")
    private List<Book> bookList;

    @Column(name ="DATE_CREATED")
    private LocalDateTime dateCreated;


    @Column(name ="PAID")
    private Boolean paid;

    @Column(name ="STATUS")
    @Enumerated(EnumType.STRING)
    private CartStatus cartStatus;

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    public Cart(LibraryUser libraryUser) {
        this.dateCreated = LocalDateTime.now();
        this.libraryUser = libraryUser;
        this.bookList = new ArrayList<>();
        this.paid = false;
        this.cartStatus=CartStatus.CREATED;
    }
}
package com.lms.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "WISH")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class Wish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "book_id")
    private Long bookId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "book_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private Book book;

    @Column(name ="DATE_CREATED")
    private LocalDateTime dateCreated;

    public Wish(){
        this.dateCreated = LocalDateTime.now();
    }
}

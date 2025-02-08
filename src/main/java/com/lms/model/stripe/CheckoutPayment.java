package com.lms.model.stripe;

import com.lms.model.Book;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.checkerframework.common.aliasing.qual.Unique;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "checkout")
public class  CheckoutPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
    // the product name
    @Unique
    private String name;
    //  currency like usd, eur ...
    private String currency;
    // our success and cancel url stripe will redirect to this links
    private String successUrl;
    private String cancelUrl;
    private long amount;
    private long quantity;
    @Column(name ="DATE_CREATED")
    private Date dateCreated;
    @ManyToMany
    @Column(name = "paid_books")
    private List<Book> books;
}



package com.lms.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "publisher")
public class Publisher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "code", length = 6)
    private String code;

    @NotNull
    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "email", length = 100, unique = true)
    private String email;

    @Column(name = "phone_number", length = 100, unique = true)
    private String phoneNumber;

    @Column(name = "link_website", length = 100)
    private String linkWebsite;

//    @OneToMany(mappedBy = "publisher", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    private List<Book> books;
}

package com.lms.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "Comment")
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"dateCreated"},
        allowGetters = true)
@Data
public class Comment {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
    private String name;
    @Column(length = 600)
    private String content;
    private String username;
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date dateCreated;
    private int likes;
    private int dislikes;
    @NotNull
    @JoinColumn(name = "book_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Book book;

}

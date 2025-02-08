package com.lms.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Entity
@Table(name = "review")
@EntityListeners(AuditingEntityListener.class)
public class Review {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Column(name = "book_id")
	private Long bookId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "book_id", nullable = false, insertable = false, updatable = false)
	private Book book;

	@NotNull
	@Column(name = "user_id")
	private Long userId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id", nullable = false, insertable = false, updatable = false)
	private LibraryUser user;

	@NotNull
	@Column(name = "point")
	private Integer point;

	@Column(name = "content", columnDefinition = "TEXT")
	private String content;

	@CreatedDate
	@Column(name = "reviewed_date", updatable = false)
	private Date reviewedDate;

}

package com.lms.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "new")
public class NewEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Column(name = "code", length = 6)
	private String code;

	@NotNull
	@Column(name = "title", length = 100)
	private String title;

	@NotNull
	@Column(name = "author_id")
	private Long authorId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "author_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
	private Author author;

	@Column(name = "release_date")
	private Date releaseDate;

	@Column(name = "keyword")
	private String keyword;

	@Column(name = "summary")
	private String summary;

	@Column(name = "content")
	private String content;

	@Column(name = "thumnail")
	private String thumnail;

}

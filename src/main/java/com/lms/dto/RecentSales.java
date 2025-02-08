package com.lms.dto;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class RecentSales {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;
    public int bookId;
    public int bookCount;
}
package com.SemestralnaPraca.MangaShop.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "reviews")
@RequiredArgsConstructor
@Data
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private int rating;
    private String comment;

    private LocalDateTime createdAt;
    @ManyToOne
    @JoinColumn(name = "user_email")
    @JsonIgnore
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonIgnore
    private Product product;

}

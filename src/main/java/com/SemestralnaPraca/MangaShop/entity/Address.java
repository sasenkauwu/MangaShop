package com.SemestralnaPraca.MangaShop.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "address")
@RequiredArgsConstructor
@Data
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String addressLine;
    private String City;
    private String PostCode;
    private String Country;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "user_email")
    private User user;



}

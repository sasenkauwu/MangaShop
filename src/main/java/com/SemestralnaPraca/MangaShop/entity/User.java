package com.SemestralnaPraca.MangaShop.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pouzivatel")
@RequiredArgsConstructor
@Data

public class User {
    @Id
    @Column(name = "email")
    private String email;

    private String name;
    private String surname;
    private String username;
    private String password;
    private String phoneNumber;
    private boolean newsletter = false;

    private String roles;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
    private Address address;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Review> reviews = new ArrayList<>();


}

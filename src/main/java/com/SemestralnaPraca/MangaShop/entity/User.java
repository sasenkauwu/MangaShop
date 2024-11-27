package com.SemestralnaPraca.MangaShop.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

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
    private String password;
    private String phoneNumber;
    private boolean newsletter;
    //private String roles; //implementovat nieco take ked bude admin rola

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
    private Address address;

    //doimplementovat cart a reviews



}

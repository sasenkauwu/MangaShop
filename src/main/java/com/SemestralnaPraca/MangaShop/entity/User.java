package com.SemestralnaPraca.MangaShop.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.Set;

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
    //private String roles; //implementovat nieco take ked bude admin rola


    //možno zmenit na string
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_email"))
    @Column(name = "role")
    private Set<String> roles = new HashSet<>();

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
    private Address address;

    //doimplementovat cart a reviews


}

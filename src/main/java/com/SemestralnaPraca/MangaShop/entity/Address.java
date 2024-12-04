package com.SemestralnaPraca.MangaShop.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
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
    private String city;
    private String postCode;
    private String country;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "user_email")
    private User user;




}

package com.SemestralnaPraca.MangaShop.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import java.util.UUID;

@Entity
@Table(name = "products")
@RequiredArgsConstructor
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String title;
    private String description;
    private double price;
    private String category;
    private String imageURL;


    //pridat prepojenie s reviews jedneho dna

}

package com.SemestralnaPraca.MangaShop.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CartGetProductsResponseDTO {
    private UUID productId;
    private String title;
    private double price;
    private String imageURL;
    private int quantity;
    private double totalPrice;
}

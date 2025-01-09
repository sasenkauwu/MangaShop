package com.SemestralnaPraca.MangaShop.DTO;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ProductUpdateDTO {
    private UUID id;
    @NotEmpty
    private String title;
    @NotEmpty
    private String description;
    @NotEmpty
    private int price;
    @NotEmpty
    private String category;
    @NotEmpty
    private String imageURL;
}

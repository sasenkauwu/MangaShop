package com.SemestralnaPraca.MangaShop.DTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ProductUpdateDTO {
    @NotNull
    private UUID id;
    @NotEmpty
    private String title;
    @NotEmpty
    private String description;
    @NotNull
    private double price;
    @NotEmpty
    private String category;
    @NotEmpty
    private String imageURL;
}

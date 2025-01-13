package com.SemestralnaPraca.MangaShop.DTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class ProductSaveDTO {
    @NotEmpty(message = "Product title cannot be empty.")
    private String title;

    @NotEmpty(message = "Product description cannot be empty.")
    private String description;

    @NotNull(message = "Product price is required.")
    @Positive(message = "Product price must be greater than zero.")
    private Double price;

    @NotEmpty(message = "Product category is required.")
    private String category;

    @NotNull(message = "Product image is required.")
    private MultipartFile imageFile;

}

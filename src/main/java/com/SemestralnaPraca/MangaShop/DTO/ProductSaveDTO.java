package com.SemestralnaPraca.MangaShop.DTO;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class ProductSaveDTO {
    @NotEmpty
    private String title;
    @NotEmpty
    private String description;
    @NotEmpty
    private double price;
    @NotEmpty
    private String category;
    @NotEmpty
    private MultipartFile imageFile;

}

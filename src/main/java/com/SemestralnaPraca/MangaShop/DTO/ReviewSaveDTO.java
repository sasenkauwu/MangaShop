package com.SemestralnaPraca.MangaShop.DTO;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ReviewSaveDTO {
    @NotNull(message = "Review rating cannot be empty.")
    @Min(value = 1, message = "Review rating must be at least 1.")
    @Max(value = 5, message = "Review rating must be at most 5.")
    private int rating;

    @NotEmpty(message = "Review comment is required.")
    private String comment;

    @NotNull(message = "Product ID is required.")
    private UUID productId;
}

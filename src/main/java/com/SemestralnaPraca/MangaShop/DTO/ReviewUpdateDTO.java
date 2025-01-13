package com.SemestralnaPraca.MangaShop.DTO;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewUpdateDTO {

    @NotEmpty(message = "Review comment cannot be empty.")
    private String comment;

    @NotNull(message = "Review rating cannot be empty.")
    @Min(value = 1, message = "Review rating must be at least 1.")
    @Max(value = 5, message = "Review rating must be at most 5.")
    private int rating;
}

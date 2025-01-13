package com.SemestralnaPraca.MangaShop.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class ReviewResponseDTO {
    private UUID reviewId;
    private String comment;
    private int rating;
    private String userEmail;
    private LocalDateTime reviewDate;
}

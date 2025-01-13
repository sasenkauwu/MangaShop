package com.SemestralnaPraca.MangaShop.controllers;

import com.SemestralnaPraca.MangaShop.DTO.ReviewResponseDTO;
import com.SemestralnaPraca.MangaShop.DTO.ReviewSaveDTO;
import com.SemestralnaPraca.MangaShop.DTO.ReviewUpdateDTO;
import com.SemestralnaPraca.MangaShop.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService ;

    @PostMapping("/save")
    public ResponseEntity<?> saveReview(@RequestBody @Valid ReviewSaveDTO reviewSaveDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("Invalid data provided.");
        }
        try {
            reviewService.saveReview(reviewSaveDTO);
            return ResponseEntity.ok("Review saved successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{reviewId}")
    public ResponseEntity<?> deleteReview(@PathVariable UUID reviewId) {
        try {
            reviewService.deleteReview(reviewId);
            return ResponseEntity.ok("Review deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/update/{reviewId}")
    public ResponseEntity<?> updateReview(@PathVariable UUID reviewId, @RequestBody @Valid ReviewUpdateDTO reviewUpdateDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("Invalid data provided.");
        }
        try {
            reviewService.updateReview(reviewUpdateDTO, reviewId);
            return ResponseEntity.ok("Review updated successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/getAll/{productId}")
    public List<ReviewResponseDTO> getAllReviews(@PathVariable UUID productId) {
        return reviewService.findAllReviews(productId);
    }


}

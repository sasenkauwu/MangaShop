package com.SemestralnaPraca.MangaShop.service;

import com.SemestralnaPraca.MangaShop.DTO.ReviewResponseDTO;
import com.SemestralnaPraca.MangaShop.DTO.ReviewSaveDTO;
import com.SemestralnaPraca.MangaShop.DTO.ReviewUpdateDTO;
import com.SemestralnaPraca.MangaShop.entity.Product;
import com.SemestralnaPraca.MangaShop.entity.Review;
import com.SemestralnaPraca.MangaShop.entity.User;
import com.SemestralnaPraca.MangaShop.repository.ProductRepository;
import com.SemestralnaPraca.MangaShop.repository.ReviewRepository;
import com.SemestralnaPraca.MangaShop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;


    public void saveReview(ReviewSaveDTO reviewSaveDTO) {
        Product product = productRepository.findById(reviewSaveDTO.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Review review = new Review();
        review.setComment(reviewSaveDTO.getComment());
        review.setRating(reviewSaveDTO.getRating());
        review.setProduct(product);
        review.setUser(user);
        review.setCreatedAt(LocalDateTime.now());

        product.getReviews().add(review);
        product.setRating(this.calculateNewRating(product.getId()));
        user.getReviews().add(review);

        reviewRepository.save(review);
    }

    public void deleteReview(UUID reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        Product product = review.getProduct();
        User user = review.getUser();

        product.getReviews().remove(review);
        user.getReviews().remove(review);

        product.setRating(this.calculateNewRating(product.getId()));

        reviewRepository.delete(review);
    }

    public void updateReview(ReviewUpdateDTO reviewUpdateDTO, UUID reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        validateReviewAuthor(review);

        review.setRating(reviewUpdateDTO.getRating());
        review.setComment(reviewUpdateDTO.getComment());
        review.setCreatedAt(LocalDateTime.now());

        review.getProduct().setRating(calculateNewRating(review.getProduct().getId()));
        reviewRepository.save(review);
    }

    public List<ReviewResponseDTO> findAllReviews(UUID productId) {
        List<Review> reviews = reviewRepository.findAllByProductId(productId);
        return reviews.stream().map(this::toReviewResponseDTO).toList();
    }

    public double calculateNewRating(UUID productId) {
        List<Review> reviews = reviewRepository.findAllByProductId(productId);
        double average = reviews.stream()
                .mapToDouble(Review::getRating)
                .average()
                .orElse(0.0);
        return BigDecimal.valueOf(average)
                .setScale(1, RoundingMode.HALF_UP)
                .doubleValue();
    }

    private void validateReviewAuthor(Review review) {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!review.getUser().getEmail().equals(currentUserEmail)) {
            throw new RuntimeException("Not authorized");
        }
    }


    public ReviewResponseDTO toReviewResponseDTO(Review review) {
        ReviewResponseDTO reviewResponseDTO = new ReviewResponseDTO();
        reviewResponseDTO.setReviewId(review.getId());
        reviewResponseDTO.setComment(review.getComment());
        reviewResponseDTO.setRating(review.getRating());
        reviewResponseDTO.setUserEmail(review.getUser().getEmail());
        reviewResponseDTO.setReviewDate(review.getCreatedAt());

        return reviewResponseDTO;
    }

}

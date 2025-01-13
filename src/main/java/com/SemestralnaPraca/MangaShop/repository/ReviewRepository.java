package com.SemestralnaPraca.MangaShop.repository;

import com.SemestralnaPraca.MangaShop.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {
    List<Review> findAllByProductId(UUID productId);
}

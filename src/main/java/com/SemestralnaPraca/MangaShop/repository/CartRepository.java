package com.SemestralnaPraca.MangaShop.repository;

import com.SemestralnaPraca.MangaShop.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartRepository  extends JpaRepository<Cart, UUID> {
    Optional<Cart> findByUserEmail(String email);
}

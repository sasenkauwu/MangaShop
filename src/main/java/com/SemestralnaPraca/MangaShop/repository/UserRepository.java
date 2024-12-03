package com.SemestralnaPraca.MangaShop.repository;

import com.SemestralnaPraca.MangaShop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    User findUserByEmail(String email);
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
    void deleteByEmail(String email);
}


package com.SemestralnaPraca.MangaShop.service;

import com.SemestralnaPraca.MangaShop.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
}

package com.SemestralnaPraca.MangaShop.controllers;

import com.SemestralnaPraca.MangaShop.DTO.CartGetProductsResponseDTO;
import com.SemestralnaPraca.MangaShop.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @PostMapping("/add/{productId}")
    public ResponseEntity<?> save(@PathVariable UUID productId) {
        try {
            cartService.addProductToCart(productId);
            return ResponseEntity.ok().body("Item added to the cart successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<?> delete(@PathVariable UUID productId) {
        try {
            cartService.removeProductFromCart(productId);
            return ResponseEntity.ok().body("Item deleted from the cart successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/clear")
    public ResponseEntity<?> clear() {
        try {
            cartService.clearCart();
            return ResponseEntity.ok().body("Cart cleared successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/getAll")
    public List<CartGetProductsResponseDTO> getAllReviews() {
        return cartService.getItemsInCart();
    }
}

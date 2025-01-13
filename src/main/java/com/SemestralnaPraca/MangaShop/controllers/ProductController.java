package com.SemestralnaPraca.MangaShop.controllers;

import com.SemestralnaPraca.MangaShop.DTO.ProductSaveDTO;
import com.SemestralnaPraca.MangaShop.DTO.ProductUpdateDTO;
import com.SemestralnaPraca.MangaShop.entity.Product;
import com.SemestralnaPraca.MangaShop.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/{id}")
    public Product getProduct(@PathVariable UUID id) {
        return productService.getProduct(id);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable UUID id) {
        productService.deleteProduct(id);
    }

    @PutMapping("/update/{id}")
    public void updateProduct(@RequestBody @Valid ProductUpdateDTO productUpdateDTO) {
        productService.updateProduct(productUpdateDTO);
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@ModelAttribute @Valid ProductSaveDTO productSaveDTO) {
        try {
            UUID id = productService.saveProduct(productSaveDTO);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}

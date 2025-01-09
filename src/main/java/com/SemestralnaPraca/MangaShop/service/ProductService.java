package com.SemestralnaPraca.MangaShop.service;

import com.SemestralnaPraca.MangaShop.DTO.ProductSaveDTO;
import com.SemestralnaPraca.MangaShop.DTO.ProductUpdateDTO;
import com.SemestralnaPraca.MangaShop.entity.Product;
import com.SemestralnaPraca.MangaShop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;

    @Value("${upload.path}")
    private String uploadPath;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProduct(UUID id) {
        return productRepository.findById(id).get();
    }

    public void deleteProduct(UUID id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
        } else {
            throw new RuntimeException("Product not found");
        }
    }

    public void updateProduct(ProductUpdateDTO productUpdateDTO) {
        if (productRepository.existsById(productUpdateDTO.getId())) {
            Product product = productRepository.findById(productUpdateDTO.getId()).get();

            product.setTitle(productUpdateDTO.getTitle());
            product.setDescription(productUpdateDTO.getDescription());
            product.setPrice(productUpdateDTO.getPrice());
            product.setCategory(productUpdateDTO.getCategory());
            productRepository.save(product);
        } else {
            throw new RuntimeException("Product not found");
        }
    }

    public UUID saveProduct(ProductSaveDTO productSaveDTO) throws IOException {
        Product product = new Product();
        product.setTitle(productSaveDTO.getTitle());
        product.setDescription(productSaveDTO.getDescription());
        product.setPrice(productSaveDTO.getPrice());
        product.setCategory(productSaveDTO.getCategory());

        MultipartFile file = productSaveDTO.getImageFile();
        if (file != null && !file.isEmpty()) {
            //String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            String fileName = file.getOriginalFilename();
            System.out.println(fileName);   //debug
            Path filePath = Paths.get(uploadPath, fileName);
            System.out.println(filePath);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            product.setImageURL("/images/" + fileName);
        }

        return productRepository.save(product).getId();
    }
}

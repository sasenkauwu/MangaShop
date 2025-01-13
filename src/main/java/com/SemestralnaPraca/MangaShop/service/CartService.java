package com.SemestralnaPraca.MangaShop.service;

import com.SemestralnaPraca.MangaShop.DTO.CartGetProductsResponseDTO;
import com.SemestralnaPraca.MangaShop.entity.Cart;
import com.SemestralnaPraca.MangaShop.entity.CartProduct;
import com.SemestralnaPraca.MangaShop.entity.Product;
import com.SemestralnaPraca.MangaShop.entity.User;
import com.SemestralnaPraca.MangaShop.repository.CartRepository;
import com.SemestralnaPraca.MangaShop.repository.ProductRepository;
import com.SemestralnaPraca.MangaShop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public void addProductToCart(UUID productId) {
        User user = userRepository.findUserByEmail(
                SecurityContextHolder.getContext().getAuthentication().getName()
        );

        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        Cart cart = cartRepository.findByUserEmail(user.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        CartProduct cartProduct = null;

        for (CartProduct cp : cart.getCartProducts()) {
            if (cp.getProduct().getId().equals(productId)) {
                cartProduct = cp;
                break;
            }
        }

        if (cartProduct == null) {
            cartProduct = new CartProduct();
            cartProduct.setCart(cart);
            cartProduct.setProduct(product);
            cartProduct.setQuantity(0);
            cart.getCartProducts().add(cartProduct);
        }

        cartProduct.setQuantity(cartProduct.getQuantity() + 1);

        updateTotalPrice(cart);

        cartRepository.save(cart);
    }



    public void removeProductFromCart(UUID productId) {
        User user = userRepository.findUserByEmail(
                SecurityContextHolder.getContext().getAuthentication().getName()
        );

        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        Cart cart = cartRepository.findByUserEmail(user.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));

        CartProduct cartProductToRemove = null;

        for (CartProduct cp : cart.getCartProducts()) {
            if (cp.getProduct().getId().equals(productId)) {
                cartProductToRemove = cp;
                break;
            }
        }

        if (cartProductToRemove == null) {
            throw new IllegalArgumentException("Product not in cart");
        }

        cart.getCartProducts().remove(cartProductToRemove);

        updateTotalPrice(cart);

        cartRepository.save(cart);
    }

    private void updateTotalPrice(Cart cart) {
        double totalPrice = 0.0;

        for (CartProduct cp : cart.getCartProducts()) {
            totalPrice += cp.getProduct().getPrice() * cp.getQuantity();
        }

        cart.setTotalPrice(totalPrice);
    }

    public void clearCart() {
        User user = userRepository.findUserByEmail(
                SecurityContextHolder.getContext().getAuthentication().getName()
        );

        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        Cart cart = cartRepository.findByUserEmail(user.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));

        cart.getCartProducts().clear();
        cart.setTotalPrice(0.0);

        cartRepository.save(cart);
    }

    public List<CartGetProductsResponseDTO> getItemsInCart() {
        User user = userRepository.findUserByEmail(
                SecurityContextHolder.getContext().getAuthentication().getName()
        );

        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        Cart cart = cartRepository.findByUserEmail(user.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));

        List<CartGetProductsResponseDTO> productDTOs = new ArrayList<>();

        for (CartProduct cp : cart.getCartProducts()) {
            CartGetProductsResponseDTO dto = new CartGetProductsResponseDTO();
            dto.setProductId(cp.getProduct().getId());
            dto.setTitle(cp.getProduct().getTitle());
            dto.setPrice(cp.getProduct().getPrice());
            dto.setImageURL(cp.getProduct().getImageURL());
            dto.setQuantity(cp.getQuantity());
            dto.setTotalPrice(cart.getTotalPrice());
            productDTOs.add(dto);
        }

        return productDTOs;
    }


}

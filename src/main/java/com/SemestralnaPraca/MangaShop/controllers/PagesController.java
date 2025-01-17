package com.SemestralnaPraca.MangaShop.controllers;

import com.SemestralnaPraca.MangaShop.service.ProductService;
import com.SemestralnaPraca.MangaShop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;


@RequiredArgsConstructor
@Controller
public class PagesController {
    private final ProductService productService;
    private final UserService userService;


    @GetMapping("/aboutUs")
    public String showAboutUs(Model model) {
        addAuthAttributes(model);
        return "aboutUs";
    }

    @GetMapping("/contacts")
    public String showContacts(Model model) {
        addAuthAttributes(model);
        return "contacts";
    }

    @GetMapping("/")
    public String showIndex(Model model) {
        addAuthAttributes(model);
        return "index";
    }

    @GetMapping("/login")
    public String showLogin(Model model) {
        addAuthAttributes(model);
        return "login";
    }

    @GetMapping("/privacyPolicy")
    public String showPrivacyPolicy(Model model) {
        addAuthAttributes(model);
        return "privacyPolicy";
    }

    @GetMapping("/products")
    public String showProducts(Model model) {
        addAuthAttributes(model);
        model.addAttribute("products", productService.getAllProducts());
        return "products";
    }

    @GetMapping("/product/{id}")
    public String showProductDetails(@PathVariable UUID id, Model model) {
        addAuthAttributes(model);
        model.addAttribute("product", productService.getProduct(id));
        return "productDetails";
    }

    @GetMapping("/products/search")
    public String searchProducts(@RequestParam String query, Model model) {
        addAuthAttributes(model);
        model.addAttribute("products", productService.searchProducts(query));
        return "products";
    }

    @GetMapping("/products/category")
    public String filterProductsByCategory(@RequestParam String category, Model model) {
        addAuthAttributes(model);
        model.addAttribute("products", productService.getProductsByCategory(category));
        return "products";
    }

    @GetMapping("/profile")
    public String showProfile(Model model) {
        addAuthAttributes(model);
        return "profile";
    }

    @GetMapping("/registration")
    public String showRegistration(Model model) {
        addAuthAttributes(model);
        return "registration";
    }

    @GetMapping("/returnPolicy")
    public String showReturnPolicy(Model model) {
        addAuthAttributes(model);
        return "returnPolicy";
    }

    @GetMapping("/changePassword")
    public String showChangePassword(Model model) {
        addAuthAttributes(model);
        return "changePassword";
    }

    @GetMapping("/users")
    public String showUsers(Model model) {
        addAuthAttributes(model);
        model.addAttribute("users", userService.getAllUsers());
        return "users";
    }

    @GetMapping("/cart")
    public String showCart(Model model) {
        addAuthAttributes(model);
        return "cart";
    }

    private void addAuthAttributes(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        boolean isAuthenticated = (authentication != null &&
                !(authentication instanceof AnonymousAuthenticationToken) &&
                authentication.isAuthenticated());
        model.addAttribute("isAuthenticated", isAuthenticated);

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("email", email);

        boolean isAdmin = isAuthenticated && authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
        model.addAttribute("isAdmin", isAdmin);

        System.out.println("Is authenticated: " + isAuthenticated + ", Email: " + email + ", Is Admin: " + isAdmin);
    }

}

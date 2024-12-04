package com.SemestralnaPraca.MangaShop.controllers;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PagesController {


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

    @GetMapping("/deleteUser")
    public String showDeleteUser(Model model) {
        addAuthAttributes(model);
        return "deleteUser";
    }

    @GetMapping("/changePassword")
    public String showChangePassword(Model model) {
        addAuthAttributes(model);
        return "changePassword";
    }

    @GetMapping("/updateUser")
    public String showUpdateUser(Model model) {
        addAuthAttributes(model);
        return "updateUser";
    }

    private void addAuthAttributes(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        boolean isAuthenticated = (authentication != null &&
                !(authentication instanceof AnonymousAuthenticationToken) &&
                authentication.isAuthenticated());
        model.addAttribute("isAuthenticated", isAuthenticated);

        String email = isAuthenticated ? authentication.getName() : "Guest";  // Pridanie správy o nezistenom používateľovi
        model.addAttribute("email", email);

        boolean isAdmin = isAuthenticated && authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
        model.addAttribute("isAdmin", isAdmin);

        System.out.println("Is authenticated: " + isAuthenticated + ", Email: " + email + ", Is Admin: " + isAdmin);
    }

}

package com.SemestralnaPraca.MangaShop.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PagesController {


    @GetMapping("/aboutUs")
    public String showAboutUs(Model model) {
        return "aboutUs";
    }

    @GetMapping("/contacts")
    public String showContacts(Model model) {
        return "contacts";
    }

    @GetMapping("/")
    public String showIndex(Model model) {
        return "index";
    }

    @GetMapping("/login")
    public String showLogin(Model model) {
        return "login";
    }

    @GetMapping("/privacyPolicy")
    public String showPrivacyPolicy(Model model) {
        return "privacyPolicy";
    }

    @GetMapping("/products")
    public String showProducts(Model model) {
        return "products";
    }

    @GetMapping("/profile")
    public String showProfile(Model model) {
        return "profile";
    }

    @GetMapping("/registration")
    public String showRegistration(Model model) {
        return "registration";
    }

    @GetMapping("/returnPolicy")
    public String showReturnPolicy(Model model) {
        return "returnPolicy";
    }
}

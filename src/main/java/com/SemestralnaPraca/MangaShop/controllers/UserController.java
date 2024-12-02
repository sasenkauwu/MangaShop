package com.SemestralnaPraca.MangaShop.controllers;

import com.SemestralnaPraca.MangaShop.DTO.UserLoginDTO;
import com.SemestralnaPraca.MangaShop.DTO.UserRegistrationDTO;
import com.SemestralnaPraca.MangaShop.entity.User;
import com.SemestralnaPraca.MangaShop.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;



import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/{email}")
    public User getUser(@PathVariable String email) {
        return userService.getUser(email);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody @Valid UserRegistrationDTO userRegistrationDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("Invalid data provided.");
        }
        try {
            userService.registerNewUser(userRegistrationDTO);
            return ResponseEntity.ok("User registered successfully");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDTO loginRequest, BindingResult  bindingResult, HttpSession session) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("Invalid data provided.");
        }

        try {
            User user = userService.authenticateUser(loginRequest.getEmail(), loginRequest.getPassword());
            session.setAttribute("user", user); // Uložíme používateľa do session
            return ResponseEntity.ok("Login successful");
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }
    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        // Vyčistíme reláciu
        session.invalidate();
        return ResponseEntity.ok("Logout successful");
    }

    @GetMapping("/me")
    public ResponseEntity<String> getLoggedInUser(HttpSession session) {
        String user = (String) session.getAttribute("user");
        if (user != null) {
            return ResponseEntity.ok("Logged in as: " + user);
        } else {
            return ResponseEntity.status(401).body("Not logged in");
        }
    }
}




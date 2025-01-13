package com.SemestralnaPraca.MangaShop.controllers;

import com.SemestralnaPraca.MangaShop.DTO.*;
import com.SemestralnaPraca.MangaShop.entity.User;
import com.SemestralnaPraca.MangaShop.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;



import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
    public ResponseEntity<?> loginUser(@RequestBody @Valid UserLoginDTO userLoginDTO, BindingResult  bindingResult, HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("Invalid data provided.");
        }

        try {
            String token = userService.authenticateUser(userLoginDTO);
            Cookie authCookie = new Cookie("jwtToken", token);
            authCookie.setSecure(true);
            authCookie.setHttpOnly(true);
            authCookie.setPath("/");
            authCookie.setMaxAge(24 * 60 * 60); // 24 hodín
            response.addCookie(authCookie);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid username or password.");
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("jwtToken", null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return ResponseEntity.ok("User Logged out successfully");
    }

    @GetMapping("/me")
    public ResponseEntity<Object> getCurrentUser() {
        Optional<User> currentUser = userService.getCurrentUser();

        if (currentUser.isPresent()) {
            return ResponseEntity.ok(currentUser.get());
        } else {
            return ResponseEntity.status(401).body("User is not authenticated.");
        }
    }

    @GetMapping("/meDTO")
    public ResponseEntity<Object> getCurrentUserDTO() {
        Optional<CurrentUserResponseDTO> currentUserResponseDTO = userService.getCurrentUserResponseDTO();

        if (currentUserResponseDTO.isPresent()) {
            return ResponseEntity.ok(currentUserResponseDTO.get());
        } else {
            return ResponseEntity.status(401).body("User is not authenticated.");
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody @Valid UserUpdateDTO userUpdateDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("Invalid data provided.");
        }

        try {
            userService.updateUser(userUpdateDTO);
            return ResponseEntity.ok("User information updated successfully");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestBody @Valid UserDeleteDTO userDeleteDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("Invalid password.");
        }

        try {
            userService.deleteUser(userDeleteDTO);
            return ResponseEntity.ok("User was deleted successfully");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/changePassword")
    public ResponseEntity<?> changePassword(@RequestBody @Valid UserPasswordChangeDTO userPasswordChangeDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("Invalid data provided.");
        }
        try {
            userService.changePassword(userPasswordChangeDTO);
            return ResponseEntity.ok("Password was chaneged successfully");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    @PostMapping("/subscribe/{email}")
    public ResponseEntity<?> subscribeNewsletter(@PathVariable String email) {
        try {
            userService.subscribeNewsletter(email);
            return ResponseEntity.ok("Newsletter subscribed successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}




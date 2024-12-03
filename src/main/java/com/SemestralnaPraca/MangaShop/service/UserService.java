package com.SemestralnaPraca.MangaShop.service;

import com.SemestralnaPraca.MangaShop.DTO.*;
import com.SemestralnaPraca.MangaShop.entity.Address;
import com.SemestralnaPraca.MangaShop.entity.User;
import com.SemestralnaPraca.MangaShop.repository.AddressRepository;
import com.SemestralnaPraca.MangaShop.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    public User getUser(String email) {
        return userRepository.findUserByEmail(email);
    }

    public String registerNewUser(@Valid UserRegistrationDTO userRegistrationDTO) {
        if (userRepository.existsByEmail(userRegistrationDTO.getEmail())) {
            throw new RuntimeException("User with this email already exists");
        }

        User user = new User();
        user.setEmail(userRegistrationDTO.getEmail());
        user.setName(userRegistrationDTO.getName());
        user.setSurname(userRegistrationDTO.getSurname());
        user.setUsername(userRegistrationDTO.getUsername());

        String hashPassword = passwordEncoder.encode(userRegistrationDTO.getPassword());
        user.setPassword(hashPassword);

        user.setPhoneNumber(userRegistrationDTO.getPhoneNumber());
        if (userRegistrationDTO.getEmail().endsWith("@mangashop.com")) {
            user.setRoles(Collections.singleton("ROLE_ADMIN"));
        } else {
            user.setRoles(Collections.singleton("ROLE_USER"));
        }

        Address address = new Address();
        address.setAddressLine(userRegistrationDTO.getAddressLine());
        address.setCity(userRegistrationDTO.getCity());
        address.setPostCode(userRegistrationDTO.getPostCode());
        address.setCountry(userRegistrationDTO.getCountry());

        address.setUser(user);
        user.setAddress(address);

        //pridat cart

        return userRepository.save(user).getEmail();
    }



    /*public User authenticateUser(String email, String password) {
        System.out.println("Checking for email:" + email);
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(optionalUser.isEmpty()) {
            System.out.println("no match for email:" + email);
            throw new BadCredentialsException("The user with the given email does not exist.");
        }

        User user = optionalUser.get();
        System.out.println("Checking for email:" + email + " password:" + password);
        boolean passwordMatch = passwordEncoder.matches(password, user.getPassword());
        System.out.println("Password are match:" + passwordMatch);

        if (!passwordMatch) {
            throw new BadCredentialsException("The user with the given email does not exist.");
        }


        return user;
    }*/

    public String authenticateUser(UserLoginDTO userLoginDTO) {
        System.out.println("Checking for email:" + userLoginDTO.getEmail());
        User user = userRepository.findByEmail(userLoginDTO.getEmail())
                .orElseThrow(() -> new BadCredentialsException("User not found"));

        System.out.println("Checking for email:" + userLoginDTO.getEmail() + " password:" + userLoginDTO.getPassword());
        if (!passwordEncoder.matches(userLoginDTO.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }
        System.out.println("Password are match:" + passwordEncoder.matches(userLoginDTO.getPassword(), user.getPassword()));

        // Generovanie JWT tokenu
        return generateJwtToken(user);
    }

    private String generateJwtToken(User user) {
        // Generovanie JWT tokenu pre autentifikáciu
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("roles", user.getRoles())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 24 hodín
                .signWith(SignatureAlgorithm.HS512, "yourSecretKey")
                .compact();
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void updateUser(UserUpdateDTO userUpdateDTO) {
        Optional<User> optionalUser = userRepository.findByEmail(userUpdateDTO.getEmail());
        if(optionalUser.isEmpty()) {
            System.out.println("no match for email:" + userUpdateDTO.getEmail());
            throw new BadCredentialsException("The user with the given email does not exist.");
        }

        User user1 = optionalUser.get();

        user1.setEmail(userUpdateDTO.getEmail());
        user1.setName(userUpdateDTO.getName());
        user1.setSurname(userUpdateDTO.getSurname());
        user1.setUsername(userUpdateDTO.getUsername());

        user1.setPhoneNumber(userUpdateDTO.getPhoneNumber());
        if (userUpdateDTO.getEmail().endsWith("@mangashop.com")) {
            user1.setRoles(Collections.singleton("ROLE_ADMIN"));
        } else {
            user1.setRoles(Collections.singleton("ROLE_USER"));
        }

        Address address = new Address();
        address.setAddressLine(userUpdateDTO.getAddressLine());
        address.setCity(userUpdateDTO.getCity());
        address.setPostCode(userUpdateDTO.getPostCode());
        address.setCountry(userUpdateDTO.getCountry());

        address.setUser(user1);
        user1.setAddress(address);

        userRepository.save(user1);
        addressRepository.save(address);
    }

    public void deleteUser(UserDeleteDTO deleteDTO) {
        if (!userRepository.existsByEmail(deleteDTO.getEmail())) {
            throw new BadCredentialsException("User does not exists!");
        } else {
            if (passwordEncoder.matches(deleteDTO.getPassword(), userRepository.findUserByEmail(deleteDTO.getEmail()).getPassword())) {
                userRepository.deleteByEmail(deleteDTO.getEmail());
            } else {
                throw new BadCredentialsException("Invalid password.");
            }
        }
    }






}

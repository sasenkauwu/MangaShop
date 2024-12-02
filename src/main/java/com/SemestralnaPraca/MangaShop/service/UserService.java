package com.SemestralnaPraca.MangaShop.service;

import com.SemestralnaPraca.MangaShop.DTO.UserRegistrationDTO;
import com.SemestralnaPraca.MangaShop.entity.Address;
import com.SemestralnaPraca.MangaShop.entity.User;
import com.SemestralnaPraca.MangaShop.repository.AddressRepository;
import com.SemestralnaPraca.MangaShop.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
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



    public User authenticateUser(String email, String password) {
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
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}

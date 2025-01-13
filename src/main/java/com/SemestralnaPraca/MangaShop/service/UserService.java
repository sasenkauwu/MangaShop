package com.SemestralnaPraca.MangaShop.service;

import com.SemestralnaPraca.MangaShop.DTO.*;
import com.SemestralnaPraca.MangaShop.config.CustomJwtUtil;
import com.SemestralnaPraca.MangaShop.entity.Address;
import com.SemestralnaPraca.MangaShop.entity.Cart;
import com.SemestralnaPraca.MangaShop.entity.User;
import com.SemestralnaPraca.MangaShop.repository.AddressRepository;
import com.SemestralnaPraca.MangaShop.repository.CartRepository;
import com.SemestralnaPraca.MangaShop.repository.UserRepository;
import jakarta.transaction.Transactional;
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

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final CartRepository cartRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final AuthenticationManager authenticationManager;



    public User getUser(String email) {
        return userRepository.findUserByEmail(email);
    }

    public String registerNewUser(@Valid UserRegistrationDTO userRegistrationDTO) {
        if (userRepository.existsByEmail(userRegistrationDTO.getEmail())) {
            throw new IllegalStateException("User with this email already exists");
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
            user.setRoles("ROLE_USER,ROLE_ADMIN");
        } else {
            user.setRoles("ROLE_USER");
        }

        Address address = new Address();
        address.setAddressLine(userRegistrationDTO.getAddressLine());
        address.setCity(userRegistrationDTO.getCity());
        address.setPostCode(userRegistrationDTO.getPostCode());
        address.setCountry(userRegistrationDTO.getCountry());

        address.setUser(user);
        user.setAddress(address);

        userRepository.save(user);

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setTotalPrice(0.0);
        cartRepository.save(cart);

        return user.getEmail();
    }


    public String authenticateUser(UserLoginDTO userLoginDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userLoginDTO.getEmail(), userLoginDTO.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        System.out.println(SecurityContextHolder.getContext().getAuthentication());
        return CustomJwtUtil.generateToken(userLoginDTO.getEmail());
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

        User user = optionalUser.get();

        user.setEmail(userUpdateDTO.getEmail());
        user.setName(userUpdateDTO.getName());
        user.setSurname(userUpdateDTO.getSurname());
        user.setUsername(userUpdateDTO.getUsername());

        user.setPhoneNumber(userUpdateDTO.getPhoneNumber());
        if (userUpdateDTO.getEmail().endsWith("@mangashop.com")) {
            user.setRoles("ROLE_USER,ROLE_ADMIN");
        } else {
            user.setRoles("ROLE_USER");
        }

        Address address = addressRepository.findAddressByUser(user);
        if (address == null) {
            address = new Address();
            address.setUser(user);
        }

        address.setAddressLine(userUpdateDTO.getAddressLine());
        address.setCity(userUpdateDTO.getCity());
        address.setPostCode(userUpdateDTO.getPostCode());
        address.setCountry(userUpdateDTO.getCountry());

        address.setUser(user);
        user.setAddress(address);

        userRepository.save(user);
        addressRepository.save(address);
    }

    @Transactional
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

    public void changePassword(UserPasswordChangeDTO passwordChangeDTO) {
        Optional<User> optionalUser = userRepository.findByEmail(passwordChangeDTO.getEmail());
        if(optionalUser.isPresent()) {
            if (passwordEncoder.matches(passwordChangeDTO.getOldPassword(), optionalUser.get().getPassword())) {
                User user = optionalUser.get();
                String hashPassword = passwordEncoder.encode(passwordChangeDTO.getNewPassword());
                user.setPassword(hashPassword);
                userRepository.save(user);
            } else {
                throw new BadCredentialsException("Invalid old password.");
            }
        } else {
            System.out.println("no match for email:" + passwordChangeDTO.getEmail());
            throw new BadCredentialsException("The user with the given email does not exist.");
        }



    }

    public Optional<User> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication.getName() == null) {
            return Optional.empty();
        }
        return userRepository.findByEmail(authentication.getName());
    }

    public Optional<CurrentUserResponseDTO> getCurrentUserResponseDTO() {
        return getCurrentUser().map(user -> {
            CurrentUserResponseDTO currentUserResponseDTO = new CurrentUserResponseDTO();
            currentUserResponseDTO.setEmail(user.getEmail());
            currentUserResponseDTO.setName(user.getName());
            currentUserResponseDTO.setSurname(user.getSurname());
            currentUserResponseDTO.setUsername(user.getUsername());
            currentUserResponseDTO.setPhoneNumber(user.getPhoneNumber());

            if (user.getRoles() != null) {
                currentUserResponseDTO.setAddressLine(user.getAddress().getAddressLine());
                currentUserResponseDTO.setCity(user.getAddress().getCity());
                currentUserResponseDTO.setPostCode(user.getAddress().getPostCode());
                currentUserResponseDTO.setCountry(user.getAddress().getCountry());
            }
            return currentUserResponseDTO;
        });
    }

    public void subscribeNewsletter(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            User newUser = user.get();
            newUser.setNewsletter(true);
            userRepository.save(newUser);
        } else {
            throw new RuntimeException("The user with the given email does not exist.");
        }
    }
}

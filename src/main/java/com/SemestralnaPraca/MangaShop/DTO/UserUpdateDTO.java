package com.SemestralnaPraca.MangaShop.DTO;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateDTO {
    @NotEmpty(message = "E-mail cannot be empty.")
    @Email(message = "E-mail must be in correct format.")
    private String email;

    @NotEmpty(message = "First name cannot be empty.")
    private String name;

    @NotEmpty(message = "Last name cannot be empty.")
    private String surname;

    @NotEmpty(message = "Username cannot be empty.")
    private String username;

    @NotEmpty(message = "Phone number cannot be empty.")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits.")
    private String phoneNumber;


    @NotEmpty(message = "Address Line cannot be empty.")
    private String addressLine;

    @NotEmpty(message = "City cannot be empty.")
    private String city;

    @NotEmpty(message = "PostCode cannot be empty.")
    @Pattern(regexp = "^[0-9]{5}$", message = "Post code must be 5 digits.")
    private String postCode;

    @NotEmpty(message = "Country cannot be empty.")
    private String country;
}

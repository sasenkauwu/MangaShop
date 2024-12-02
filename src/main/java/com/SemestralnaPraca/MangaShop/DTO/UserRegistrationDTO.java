package com.SemestralnaPraca.MangaShop.DTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class UserRegistrationDTO {
    @NotEmpty(message = "E-mail cannot be empty.")
    @Email(message = "E-mail must be in correct format.")
    private String email;

    @NotEmpty(message = "First name cannot be empty.")
    private String name;
    @NotEmpty(message = "Last name cannot be empty.")
    private String surname;
    @NotEmpty
    private String username;
    @NotEmpty(message = "Password cannot be empty.")
    @Size(min = 10, max = 25, message = "Password must be between 10-25 characters long.")
    private String password;
    @NotEmpty(message = "Phone number cannot be empty.")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits.")
    private String phoneNumber;

   // @Valid
    //private AddressRegistrationDTO address;
    @NotEmpty(message = "Address Line cannot be empty.")
    private String addressLine;
    @NotEmpty(message = "City cannot be empty.")
    private String city;
    @NotEmpty(message = "PostCode cannot be empty.")
    @Size(min = 5, max = 5, message = "Postcode must be exactly 5 characters.")
    private String postCode;
    @NotEmpty(message = "Country cannot be empty.")
    private String country;

}

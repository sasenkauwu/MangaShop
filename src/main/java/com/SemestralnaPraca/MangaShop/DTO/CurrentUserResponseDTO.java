package com.SemestralnaPraca.MangaShop.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CurrentUserResponseDTO {
    private String email;
    private String name;
    private String surname;
    private String username;
    private String phoneNumber;

    private String addressLine;
    private String city;
    private String postCode;
    private String country;
}

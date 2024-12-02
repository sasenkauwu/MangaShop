package com.SemestralnaPraca.MangaShop.DTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginDTO {
    @NotEmpty(message = "Email cannot be empty.")
    private String email;
    @NotEmpty(message = "Password cannot be empty.")
    @Size(min = 10, max = 25, message = "Inavlid password.")
    private String password;
}

package com.SemestralnaPraca.MangaShop.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPasswordChangeDTO {
    @NotEmpty(message = "E-mail cannot be empty.")
    @Email(message = "E-mail must be in correct format.")
    private String email;

    @NotEmpty(message = "Password cannot be empty.")
    @Size(min = 10, max = 25, message = "Inavlid password.")
    private String oldPassword;

    @NotEmpty(message = "Password cannot be empty.")
    @Size(min = 10, max = 25, message = "Inavlid password.")
    private String newPassword;
}

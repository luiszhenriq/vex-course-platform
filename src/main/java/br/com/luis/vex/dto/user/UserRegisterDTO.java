package br.com.luis.vex.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserRegisterDTO(

        @NotBlank(message = "Este campo não pode ser vazio")
        String fullName,

        @NotBlank(message = "Este campo não pode ser vazio")
        @Email
        String email,

        @NotBlank(message = "Este campo não pode ser vazio")
        @Pattern(
                regexp = "^\\d{8,11}$",
                message = "Deve conter apenas números (mínimo 8 e máximo 11 dígitos)"
        )
        String cellphone,

        @NotBlank(message = "Este campo não pode ser vazio")
        @Pattern(
                regexp = "^\\d{11}$",
                message = "Deve conter apenas números (com 11 digítos)"
        )
        String taxId,

        @NotBlank(message = "Este campo não pode ser vazio")
        String password
) {
}

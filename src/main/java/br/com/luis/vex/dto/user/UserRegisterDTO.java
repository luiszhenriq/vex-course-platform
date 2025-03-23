package br.com.luis.vex.dto.user;

public record UserRegisterDTO(

        String fullName,

        String email,

        String password
) {
}

package br.com.luis.vex.dto.user;

import br.com.luis.vex.model.enums.UserType;

import java.util.UUID;

public record UserResponseDTO(

         UUID id,

         String fullName,

         String email,

        UserType userType
) {
}

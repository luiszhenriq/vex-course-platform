package br.com.luis.vex.dto.module;

import jakarta.validation.constraints.NotBlank;

public record ModuleUpdateDTO(

        @NotBlank(message = "Este campo não pode ser vazio")
        String title
) {
}

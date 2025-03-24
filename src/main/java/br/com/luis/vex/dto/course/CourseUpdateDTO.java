package br.com.luis.vex.dto.course;

import jakarta.validation.constraints.NotBlank;

public record CourseUpdateDTO(

        @NotBlank(message = "Este campo não pode ser vazio")
        String title,

        @NotBlank(message = "Este campo não pode ser vazio")
        String description,

        @NotBlank(message = "Este campo não pode ser vazio")
        String category
) {
}

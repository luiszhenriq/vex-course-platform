package br.com.luis.vex.dto.lesson;

import jakarta.validation.constraints.NotBlank;

public record LessonRequestDTO(

        @NotBlank(message = "Este campo não pode ser vazio")
        String title,

        @NotBlank(message = "Este campo não pode ser vazio")
        String videoUrl,

        @NotBlank(message = "Este campo não pode ser vazio")
        Integer duration) {
}

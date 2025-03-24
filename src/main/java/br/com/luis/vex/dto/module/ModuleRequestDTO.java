package br.com.luis.vex.dto.module;

import br.com.luis.vex.dto.lesson.LessonRequestDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record ModuleRequestDTO(

        @NotNull(message = "Este campo não pode ser nulo")
        UUID courseId,

        @NotBlank(message = "Este campo não pode ser vazio")
        String title,

        @NotNull(message = "Este campo não pode ser nulo")
        List<LessonRequestDTO> lessons

) {
}

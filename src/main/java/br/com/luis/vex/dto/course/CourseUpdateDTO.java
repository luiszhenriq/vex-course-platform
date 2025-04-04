package br.com.luis.vex.dto.course;

import br.com.luis.vex.model.enums.CategoryType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CourseUpdateDTO(

        @NotBlank(message = "Este campo não pode ser vazio")
        String title,

        @NotBlank(message = "Este campo não pode ser vazio")
        String description,

        @NotNull(message = "Este campo não pode ser nulo")
        CategoryType category
) {
}

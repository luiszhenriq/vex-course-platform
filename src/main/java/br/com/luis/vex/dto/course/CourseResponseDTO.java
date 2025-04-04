package br.com.luis.vex.dto.course;

import br.com.luis.vex.dto.module.ModuleResponseDTO;
import br.com.luis.vex.model.enums.CategoryType;

import java.util.List;
import java.util.UUID;

public record CourseResponseDTO(
        UUID id,

        String title,

        String description,

        CategoryType category,

        List<ModuleResponseDTO> modules
) {
}

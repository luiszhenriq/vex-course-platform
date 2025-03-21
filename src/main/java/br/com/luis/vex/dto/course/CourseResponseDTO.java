package br.com.luis.vex.dto.course;

import br.com.luis.vex.dto.module.ModuleResponseDTO;

import java.util.List;
import java.util.UUID;

public record CourseResponseDTO(
        UUID id,

        String title,

        String description,

        String category,

        List<ModuleResponseDTO> modules
) {
}

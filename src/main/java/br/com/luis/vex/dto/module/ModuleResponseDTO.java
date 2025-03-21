package br.com.luis.vex.dto.module;

import br.com.luis.vex.dto.lesson.LessonResponseDTO;

import java.util.List;
import java.util.UUID;

public record ModuleResponseDTO(
        UUID id,

        String title,

        List<LessonResponseDTO> lessons
) {
}

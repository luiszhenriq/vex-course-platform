package br.com.luis.vex.dto.module;

import br.com.luis.vex.dto.lesson.LessonRequestDTO;

import java.util.List;
import java.util.UUID;

public record ModuleRequestDTO(

        UUID courseId,

        String title,

        List<LessonRequestDTO> lessons

) {
}

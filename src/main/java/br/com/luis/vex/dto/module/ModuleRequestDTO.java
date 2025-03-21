package br.com.luis.vex.dto.module;

import br.com.luis.vex.dto.lesson.LessonRequestDTO;

import java.util.List;

public record ModuleRequestDTO(

        String title,

        List<LessonRequestDTO> lessons

) {
}

package br.com.luis.vex.dto.lesson;

import java.util.UUID;

public record LessonResponseDTO(

        UUID id,

        String title,

        String videoUrl,

        Integer duration
) {
}

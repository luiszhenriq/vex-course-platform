package br.com.luis.vex.dto.lesson;

import java.util.UUID;

public record LessonPreviewDTO(

        UUID id,

        String title,

        Integer duration
) {
}

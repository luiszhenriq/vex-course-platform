package br.com.luis.vex.dto.lesson;

public record LessonRequestDTO(
        String title,

        String videoUrl,

        Integer duration) {
}

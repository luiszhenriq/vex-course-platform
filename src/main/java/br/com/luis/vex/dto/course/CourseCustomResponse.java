package br.com.luis.vex.dto.course;

import br.com.luis.vex.model.Course;

import java.util.UUID;

public record CourseCustomResponse(
        UUID id,

        String title
) {

    public CourseCustomResponse(Course course) {
        this(
                course.getId(),
                course.getTitle()
        );
    }
}

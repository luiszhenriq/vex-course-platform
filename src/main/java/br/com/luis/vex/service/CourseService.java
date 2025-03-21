package br.com.luis.vex.service;


import br.com.luis.vex.dto.course.CourseRequestDTO;
import br.com.luis.vex.dto.course.CourseResponseDTO;
import br.com.luis.vex.dto.lesson.LessonResponseDTO;
import br.com.luis.vex.dto.module.ModuleResponseDTO;
import br.com.luis.vex.model.Course;
import br.com.luis.vex.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository repository;


    public CourseResponseDTO create(CourseRequestDTO course) {

        Course newCourse = new Course(course);

        Course savedCourse = repository.save(newCourse);

        return courseResponseDTO(savedCourse);

    }

    public CourseResponseDTO findById(UUID id) {

        Course course = repository.findById(id).
                orElseThrow(() -> new RuntimeException("Curso não Encontrado"));

        return courseResponseDTO(course);
    }


    private CourseResponseDTO courseResponseDTO(Course course) {

        return new CourseResponseDTO(
                course.getId(),
                course.getTitle(),
                course.getDescription(),
                course.getCategory(),
                course.getModules().stream().map(module -> new ModuleResponseDTO(
                        module.getId(),
                        module.getTitle(),
                        module.getLessons().stream().map(lesson -> new LessonResponseDTO(
                                lesson.getId(),
                                lesson.getTitle(),
                                lesson.getVideoUrl(),
                                lesson.getDuration()
                        )).collect(Collectors.toList())
                )).collect(Collectors.toList())
        );
    }
}

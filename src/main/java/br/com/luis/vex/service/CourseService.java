package br.com.luis.vex.service;


import br.com.luis.vex.dto.course.CourseRequestDTO;
import br.com.luis.vex.dto.course.CourseResponseDTO;
import br.com.luis.vex.dto.course.CourseUpdateDTO;
import br.com.luis.vex.dto.lesson.LessonResponseDTO;
import br.com.luis.vex.dto.module.ModuleResponseDTO;
import br.com.luis.vex.model.Course;
import br.com.luis.vex.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository repository;


    @Transactional
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

    public List<CourseResponseDTO> findAll() {

        return repository.findAll()
                .stream()
                .map(this::courseResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public CourseResponseDTO update(UUID id, CourseUpdateDTO courseUpdate) {

        Course course = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Curso não encontrado"));

        course.setTitle(courseUpdate.title());
        course.setDescription(courseUpdate.description());
        course.setCategory(courseUpdate.category());

        Course updatedCourse = repository.save(course);

        return courseResponseDTO(updatedCourse);
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

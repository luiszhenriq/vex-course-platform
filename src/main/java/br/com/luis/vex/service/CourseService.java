package br.com.luis.vex.service;


import br.com.luis.vex.dto.course.CourseRequestDTO;
import br.com.luis.vex.dto.course.CourseResponseDTO;
import br.com.luis.vex.dto.course.CourseUpdateDTO;
import br.com.luis.vex.dto.lesson.LessonPreviewDTO;
import br.com.luis.vex.dto.lesson.LessonResponseDTO;
import br.com.luis.vex.dto.module.ModuleResponseDTO;
import br.com.luis.vex.dto.user.UserResponseDTO;
import br.com.luis.vex.infra.exception.IdNotFoundException;
import br.com.luis.vex.model.Course;
import br.com.luis.vex.model.CoursePurchase;
import br.com.luis.vex.model.enums.CategoryType;
import br.com.luis.vex.repository.CoursePurchaseRepository;
import br.com.luis.vex.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository repository;
    private final UserService userService;
    private final CoursePurchaseRepository coursePurchaseRepository;

    @Transactional
    public CourseResponseDTO create(CourseRequestDTO course) {

        Course newCourse = new Course(course);

        Course savedCourse = repository.save(newCourse);

        return courseResponseDTO(savedCourse);

    }

    public CourseResponseDTO findById(UUID id) {

        Course course = repository.findById(id).
                orElseThrow(() -> new IdNotFoundException("Curso não Encontrado"));

        UserResponseDTO user = userService.getAuthenticatedUser();

        Optional<CoursePurchase> existing = coursePurchaseRepository.findByUserIdAndCourseId(user.id(), course.getId());

        List<ModuleResponseDTO> moduleDTOs = course.getModules().stream().map(module -> {
            List<?> lessonDTOs;

            if (existing.isPresent()) {
                lessonDTOs = module.getLessons().stream()
                        .map(lesson -> new LessonResponseDTO(lesson.getId(), lesson.getTitle(), lesson.getVideoUrl(), lesson.getDuration()))
                        .toList();
            } else {
                lessonDTOs = module.getLessons().stream()
                        .map(lesson -> new LessonPreviewDTO(lesson.getId(), lesson.getTitle(), lesson.getDuration()))
                        .toList();
            }

            return new ModuleResponseDTO(module.getId(), module.getTitle(), lessonDTOs);
        }).toList();

        return new CourseResponseDTO(course.getId(), course.getTitle(), course.getDescription(), course.getCategory(), moduleDTOs);
    }

    public Page<CourseResponseDTO> findAll(Pageable pageable) {

        Page<Course> courses = repository.findAll(pageable);

        return courses.map(this::courseResponseDTO);
    }

    @Transactional
    public CourseResponseDTO update(UUID id, CourseUpdateDTO courseUpdate) {

        Course course = repository.findById(id)
                .orElseThrow(() -> new IdNotFoundException("Curso não encontrado"));

        course.setTitle(courseUpdate.title());
        course.setDescription(courseUpdate.description());
        course.setCategory(courseUpdate.category());

        Course updatedCourse = repository.save(course);

        return courseResponseDTO(updatedCourse);
    }

    public Page<CourseResponseDTO> findAllCoursesByFilter(CategoryType category, String title, Pageable pageable) {

        Page<Course> courses = repository.findAllCoursesByFilter(category, title, pageable);

        return courses.map(this::courseResponseDTO);
    }

    public void deleteById(UUID id) {
        repository.deleteById(id);
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

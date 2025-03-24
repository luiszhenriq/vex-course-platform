package br.com.luis.vex.service;


import br.com.luis.vex.dto.course.CourseResponseDTO;
import br.com.luis.vex.dto.lesson.LessonResponseDTO;
import br.com.luis.vex.dto.module.ModuleRequestDTO;
import br.com.luis.vex.dto.module.ModuleResponseDTO;
import br.com.luis.vex.dto.module.ModuleUpdateDTO;
import br.com.luis.vex.infra.exception.IdNotFoundException;
import br.com.luis.vex.model.Course;
import br.com.luis.vex.model.Lesson;
import br.com.luis.vex.model.Module;
import br.com.luis.vex.repository.CourseRepository;
import br.com.luis.vex.repository.ModuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ModuleService {

    private final ModuleRepository repository;
    private final CourseRepository courseRepository;

    @Transactional
    public ModuleResponseDTO create(ModuleRequestDTO module) {

        Course course = courseRepository.findById(module.courseId())
                .orElseThrow(() -> new IdNotFoundException("Curso não encontrado"));


        Module newModule = new Module(module);

        List<Lesson> lessons = module.lessons().stream()
                .map(lesson -> {
                    Lesson newLesson = new Lesson();
                    newLesson.setTitle(lesson.title());
                    newLesson.setVideoUrl(lesson.videoUrl());
                    newLesson.setDuration(lesson.duration());
                    newLesson.setModule(newModule);
                    return newLesson;
                }).collect(Collectors.toList());

        newModule.setLessons(lessons);
        newModule.setCourse(course);

        Module savedModule = repository.save(newModule);

        return moduleResponseDTO(savedModule);
    }

    public void deleteById(UUID id) {
        repository.deleteById(id);
    }

    public ModuleResponseDTO findById(UUID id) {

        Module module = repository.findById(id)
                .orElseThrow(() -> new IdNotFoundException("Módulo do curso não encontrado"));

        return moduleResponseDTO(module);
    }

    @Transactional
    public ModuleResponseDTO update(UUID id, ModuleUpdateDTO moduleUpdate) {

        Module module = repository.findById(id)
                .orElseThrow(() -> new IdNotFoundException("Módulo do curso não encontrado"));

        module.setTitle(moduleUpdate.title());

        Module updatedModule = repository.save(module);

        return moduleResponseDTO(updatedModule);

    }

    private ModuleResponseDTO moduleResponseDTO(Module module) {

        return new ModuleResponseDTO(
                module.getId(),
                module.getTitle(),
                module.getLessons().stream().map(lesson -> new LessonResponseDTO(
                    lesson.getId(),
                    lesson.getTitle(),
                    lesson.getVideoUrl(),
                    lesson.getDuration()
                )).collect(Collectors.toList())
        );
    }
}

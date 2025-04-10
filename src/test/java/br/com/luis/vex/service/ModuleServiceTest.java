package br.com.luis.vex.service;

import br.com.luis.vex.dto.lesson.LessonRequestDTO;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ModuleServiceTest {

    @InjectMocks
    private ModuleService moduleService;

    @Mock
    private ModuleRepository moduleRepository;

    @Mock
    private CourseRepository courseRepository;


    @Test
    @DisplayName("Should return a created module with success")
    void shouldReturnACreatedModuleWithSuccess() {
        UUID courseId = UUID.randomUUID();
        Course course = new Course();
        course.setId(courseId);

        LessonRequestDTO lessonDTO = new LessonRequestDTO("Aula 1", "http://video.com/1", 120);
        ModuleRequestDTO requestDTO = new ModuleRequestDTO(courseId, "Módulo 1", List.of(lessonDTO)
        );

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

        Module savedModule = new Module();
        savedModule.setTitle("Módulo 1");
        savedModule.setCourse(course);

        Lesson lesson = new Lesson();
        lesson.setTitle("Aula 1");
        lesson.setVideoUrl("http://video.com/1");
        lesson.setDuration(120);
        lesson.setModule(savedModule);

        savedModule.setLessons(List.of(lesson));

        when(moduleRepository.save(any(Module.class))).thenReturn(savedModule);

        ModuleResponseDTO result = moduleService.create(requestDTO);

        assertNotNull(result);
        assertEquals("Módulo 1", result.title());
        assertEquals(1, result.lessons().size());

        verify(courseRepository).findById(courseId);
        verify(moduleRepository).save(any(Module.class));
    }

    @Test
    @DisplayName("Should return id not found exception when invalid course id ")
    void shouldReturnIdNotFoundExceptionWhenInvalidCourseId() {

        UUID invalidCourseId = UUID.randomUUID();
        ModuleRequestDTO requestDTO = new ModuleRequestDTO(invalidCourseId, "Módulo 1", List.of()
        );

        when(courseRepository.findById(invalidCourseId)).thenReturn(Optional.empty());

        IdNotFoundException exception = assertThrows(IdNotFoundException.class,
                () -> moduleService.create(requestDTO));

        assertEquals("Curso não encontrado", exception.getMessage());
        verify(courseRepository).findById(invalidCourseId);
        verify(moduleRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should delete a module with success")
    void shouldDeleteAModuleWithSuccess(){
        UUID moduleId = UUID.randomUUID();
        Module module = new Module();
        module.setId(moduleId);

        doNothing().when(moduleRepository).deleteById(moduleId);

        moduleService.deleteById(moduleId);

        verify(moduleRepository, times(1)).deleteById(moduleId);
    }

    @Test
    @DisplayName("Should update a module with success")
    void shouldUpdateAModuleWithSuccess() {

        UUID moduleId = UUID.randomUUID();
        Module module = new Module();
        module.setId(moduleId);
        module.setTitle("Modulo 1");

        Lesson lesson = new Lesson();
        lesson.setTitle("Aula 1");
        lesson.setVideoUrl("http://video.com/1");
        lesson.setDuration(120);
        lesson.setModule(module);

        module.setLessons(List.of(lesson));


        ModuleUpdateDTO moduleUpdateDTO = new ModuleUpdateDTO("Aula atualizada");

        when(moduleRepository.findById(moduleId)).thenReturn(Optional.of(module));
        when(moduleRepository.save(any())).thenReturn(module);

        ModuleResponseDTO response = moduleService.update(moduleId, moduleUpdateDTO);

        assertNotNull(response);
        assertEquals(ModuleResponseDTO.class, response.getClass());
        assertEquals(moduleId, response.id());
        assertEquals(moduleUpdateDTO.title(), response.title());

    }

    @Test
    @DisplayName("Should return an id not found exception when update lesson")
    void shouldReturnAnIdNotFoundExceptionWhenUpdateLesson() {

        UUID moduleId = UUID.randomUUID();
        Module module = new Module();
        module.setId(moduleId);

        when(moduleRepository.findById(moduleId)).thenReturn(Optional.empty());

        ModuleUpdateDTO moduleUpdateDTO = new ModuleUpdateDTO("Aula atualizada");


        Exception ex = assertThrows(IdNotFoundException.class, () -> {
            moduleService.update(moduleId, moduleUpdateDTO);
        });

        assertEquals("Módulo do curso não encontrado", ex.getMessage());
    }

}
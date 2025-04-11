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
import org.junit.jupiter.api.BeforeEach;
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

    private static final UUID MODULE_ID =                           UUID.randomUUID();
    private static final String MODULE_TITLE =                      "Módulo 1";
    private static final String UPDATED_MODULE_TITLE =              "Módulo Atualizado";

    private static final UUID COURSE_ID =                           UUID.randomUUID();

    private static final String LESSON_TITLE =                      "Aula 1";
    private static final String LESSON_URL =                        "http://video.com/1";
    private static final int LESSON_DURATION =                      12000;

    @InjectMocks
    private ModuleService moduleService;

    @Mock
    private ModuleRepository moduleRepository;

    @Mock
    private CourseRepository courseRepository;

    private Module module;
    private Course course;
    private Lesson lesson;
    private ModuleRequestDTO moduleRequestDTO;
    private ModuleUpdateDTO moduleUpdateDTO;

    @BeforeEach
    void setUp() {
        course = new Course();
        course.setId(COURSE_ID);

        lesson = new Lesson();
        lesson.setTitle(LESSON_TITLE);
        lesson.setVideoUrl(LESSON_URL);
        lesson.setDuration(LESSON_DURATION);

        module = new Module();
        module.setId(MODULE_ID);
        module.setTitle(MODULE_TITLE);
        module.setCourse(course);
        module.setLessons(List.of(lesson));
        lesson.setModule(module);

        LessonRequestDTO lessonDTO = new LessonRequestDTO(LESSON_TITLE, LESSON_URL, LESSON_DURATION);
        moduleRequestDTO = new ModuleRequestDTO(COURSE_ID, MODULE_TITLE, List.of(lessonDTO));
        moduleUpdateDTO = new ModuleUpdateDTO(UPDATED_MODULE_TITLE);
    }


    @Test
    @DisplayName("Should return a created module with success")
    void shouldReturnACreatedModuleWithSuccess() {
        when(courseRepository.findById(COURSE_ID)).thenReturn(Optional.of(course));
        when(moduleRepository.save(any(Module.class))).thenReturn(module);

        ModuleResponseDTO result = moduleService.create(moduleRequestDTO);

        assertNotNull(result);
        assertEquals(MODULE_TITLE, result.title());
        assertEquals(1, result.lessons().size());
        verify(courseRepository).findById(COURSE_ID);
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
        when(moduleRepository.findById(MODULE_ID)).thenReturn(Optional.of(module));
        when(moduleRepository.save(any())).thenReturn(module);

        ModuleResponseDTO response = moduleService.update(MODULE_ID, moduleUpdateDTO);

        assertNotNull(response);
        assertEquals(ModuleResponseDTO.class, response.getClass());
        assertEquals(MODULE_ID, response.id());
        assertEquals(UPDATED_MODULE_TITLE, response.title());
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
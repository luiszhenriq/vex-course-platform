package br.com.luis.vex.controller;

import br.com.luis.vex.dto.lesson.LessonRequestDTO;
import br.com.luis.vex.dto.module.ModuleRequestDTO;
import br.com.luis.vex.dto.module.ModuleResponseDTO;
import br.com.luis.vex.dto.module.ModuleUpdateDTO;
import br.com.luis.vex.model.Course;
import br.com.luis.vex.model.Lesson;
import br.com.luis.vex.model.Module;
import br.com.luis.vex.repository.CourseRepository;
import br.com.luis.vex.service.ModuleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ModuleControllerTest {

    private static final UUID MODULE_ID =                           UUID.randomUUID();
    private static final String MODULE_TITLE =                      "Módulo 1";
    private static final String UPDATED_MODULE_TITLE =              "Módulo Atualizado";

    private static final UUID COURSE_ID =                           UUID.randomUUID();

    private static final String LESSON_TITLE =                      "Aula 1";
    private static final String LESSON_URL =                        "http://video.com/1";
    private static final int LESSON_DURATION =                      12000;

    @InjectMocks
    private ModuleController moduleController;

    @Mock
    private ModuleService moduleService;

    @Mock
    private CourseRepository courseRepository;

    private br.com.luis.vex.model.Module module;
    private Course course;
    private Lesson lesson;
    private ModuleRequestDTO moduleRequestDTO;
    private ModuleUpdateDTO moduleUpdateDTO;
    private ModuleResponseDTO moduleResponseDTO;

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
        moduleResponseDTO = new ModuleResponseDTO(MODULE_ID, MODULE_TITLE, List.of());

    }

    @Test
    @DisplayName("Should return a created module with success")
    void shouldReturnACreatedModuleWithSuccess() {

        when(moduleService.create(moduleRequestDTO)).thenReturn(moduleResponseDTO);

        ResponseEntity<ModuleResponseDTO> response = moduleController.create(moduleRequestDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(moduleResponseDTO, response.getBody());
        verify(moduleService).create(moduleRequestDTO);
    }

    @Test
    @DisplayName("Should return a module by id with success")
    void shouldReturnAModuleByIdWithSuccess() {

        when(moduleService.findById(MODULE_ID)).thenReturn(moduleResponseDTO);

        ResponseEntity<ModuleResponseDTO> response = moduleController.findById(MODULE_ID);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(moduleResponseDTO, response.getBody());
        verify(moduleService).findById(MODULE_ID);
    }

    @Test
    @DisplayName("Should delete a module with success")
    void shouldDeleteAModuleWithSuccess() {
        doNothing().when(moduleService).deleteById(MODULE_ID);

        ResponseEntity<Void> response = moduleController.delete(MODULE_ID);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        verify(moduleService, times(1)).deleteById(MODULE_ID);
    }

    @Test
    @DisplayName("Should update a module with success")
    void shouldUpdateAModuleWithSuccess() {
        when(moduleService.update(MODULE_ID, moduleUpdateDTO)).thenReturn(moduleResponseDTO);

        ResponseEntity<ModuleResponseDTO> response = moduleController.update(MODULE_ID, moduleUpdateDTO);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(ModuleResponseDTO.class, response.getBody().getClass());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

}
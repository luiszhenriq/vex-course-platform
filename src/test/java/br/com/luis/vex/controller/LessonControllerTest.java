package br.com.luis.vex.controller;

import br.com.luis.vex.dto.lesson.LessonRequestDTO;
import br.com.luis.vex.dto.lesson.LessonResponseDTO;
import br.com.luis.vex.service.LessonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class LessonControllerTest {


    public static final UUID ID =                           UUID.randomUUID();
    public static final String TITLE =                      "Java básico";
    public static final String VIDEO_URL =                  "https://example.com/videos/1";
    public static final Integer DURATION =                  15000;

    @InjectMocks
    private LessonController lessonController;

    @Mock
    private LessonService lessonService;

    private LessonRequestDTO lessonRequestDTO;

    private LessonResponseDTO lessonResponseDTO;

    @BeforeEach
    void setUp() {
        lessonResponseDTO = new LessonResponseDTO(ID, TITLE, VIDEO_URL, DURATION);
        lessonRequestDTO = new LessonRequestDTO("Java Essencial", "https://example.com/java", 12000);
    }


    @Test
    @DisplayName("Should delete a lesson with success")
    void shouldDeleteALessonWithSuccess() {
        doNothing().when(lessonService).deleteById(ID);

        ResponseEntity<Void> response = lessonController.deleteById(ID);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        verify(lessonService, times(1)).deleteById(ID);
    }

    @Test
    @DisplayName("Should update a lesson with success")
    void shouldUpdateALessonWithSuccess() {
        when(lessonService.update(ID, lessonRequestDTO)).thenReturn(lessonResponseDTO);

        ResponseEntity<LessonResponseDTO> response = lessonController.update(ID, lessonRequestDTO);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(LessonResponseDTO.class, response.getBody().getClass());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

}
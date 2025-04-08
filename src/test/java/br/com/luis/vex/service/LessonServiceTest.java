package br.com.luis.vex.service;

import br.com.luis.vex.dto.lesson.LessonRequestDTO;
import br.com.luis.vex.dto.lesson.LessonResponseDTO;
import br.com.luis.vex.infra.exception.IdNotFoundException;
import br.com.luis.vex.model.Lesson;
import br.com.luis.vex.model.Module;
import br.com.luis.vex.repository.LessonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class LessonServiceTest {


    public static final UUID ID =                           UUID.randomUUID();
    public static final String TITLE =                      "Java básico";
    public static final String VIDEO_URL =                  "https://example.com/videos/1";
    public static final Integer DURATION =                  15000;

    @InjectMocks
    private LessonService lessonService;

    @Mock
    private LessonRepository lessonRepository;

    private Lesson lesson;

    private Module module;

    private LessonRequestDTO lessonRequestDTO;


    @BeforeEach
    void setUp() {
        lesson = new Lesson(ID, TITLE, VIDEO_URL, DURATION, module);
        lessonRequestDTO = new LessonRequestDTO("Java Essencial", "https://example.com/java", 12000);
    }

    @Test
    @DisplayName("Should delete a lesson with success")
    void shouldDeleteALessonWithSuccess(){
        doNothing().when(lessonRepository).deleteById(ID);

        lessonRepository.deleteById(ID);

        verify(lessonRepository, times(1)).deleteById(ID);
    }

    @Test
    @DisplayName("Should update a lesson with success")
    void shouldUpdateALessonWithSuccess() {
        when(lessonRepository.findById(ID)).thenReturn(Optional.of(lesson));
        when(lessonRepository.save(any())).thenReturn(lesson);

        LessonResponseDTO response = lessonService.update(ID, lessonRequestDTO);

        assertNotNull(response);
        assertEquals(LessonResponseDTO.class, response.getClass());
        assertEquals(ID, response.id());
        assertEquals(lessonRequestDTO.title(), response.title());
        assertEquals(lessonRequestDTO.videoUrl(), response.videoUrl());
        assertEquals(lessonRequestDTO.duration(), response.duration());

    }

    @Test
    @DisplayName("Should return an id not found exception when update lesson")
    void shouldReturnAnIdNotFoundExceptionWhenUpdateLesson() {

        when(lessonRepository.findById(ID)).thenReturn(Optional.empty());

        Exception ex = assertThrows(IdNotFoundException.class, () -> {
            lessonService.update(ID, lessonRequestDTO);
        });

        assertEquals("Aula não encontrada", ex.getMessage());
    }

}
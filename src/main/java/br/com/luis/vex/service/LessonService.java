package br.com.luis.vex.service;


import br.com.luis.vex.dto.lesson.LessonRequestDTO;
import br.com.luis.vex.dto.lesson.LessonResponseDTO;
import br.com.luis.vex.infra.exception.IdNotFoundException;
import br.com.luis.vex.model.Lesson;
import br.com.luis.vex.repository.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository repository;

    public void deleteById(UUID id) {
        repository.deleteById(id);
    }

    @Transactional
    public LessonResponseDTO update(UUID id, LessonRequestDTO lessonRequest) {

        Lesson lesson = repository.findById(id).orElseThrow(() -> new IdNotFoundException("Aula não encontrada"));

        lesson.setTitle(lessonRequest.title());
        lesson.setVideoUrl(lessonRequest.videoUrl());
        lesson.setDuration(lessonRequest.duration());

        Lesson updatedLesson = repository.save(lesson);

        return new LessonResponseDTO(
                updatedLesson.getId(),
                updatedLesson.getTitle(),
                updatedLesson.getVideoUrl(),
                updatedLesson.getDuration()
        );
    }
}

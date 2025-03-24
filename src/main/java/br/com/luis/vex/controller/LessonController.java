package br.com.luis.vex.controller;


import br.com.luis.vex.dto.lesson.LessonRequestDTO;
import br.com.luis.vex.dto.lesson.LessonResponseDTO;
import br.com.luis.vex.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/lesson")
@RequiredArgsConstructor
public class LessonController {

    private final LessonService service;

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable("id") UUID id) {
        service.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LessonResponseDTO> update(@PathVariable("id") UUID id,
                                                    @RequestBody LessonRequestDTO lesson) {
        return new ResponseEntity<>(service.update(id, lesson), HttpStatus.OK);
    }
}

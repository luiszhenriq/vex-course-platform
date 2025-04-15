package br.com.luis.vex.controller;


import br.com.luis.vex.dto.lesson.LessonRequestDTO;
import br.com.luis.vex.dto.lesson.LessonResponseDTO;
import br.com.luis.vex.service.LessonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
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


    @Operation(summary = "Delete lesson by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode="204", description="Lesson deleted successfully"),
            @ApiResponse(responseCode="401", description="Unauthorized")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable("id") UUID id) {
        service.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Update lesson")
    @ApiResponses(value = {
            @ApiResponse(responseCode="200", description="Lesson updated successfully"),
            @ApiResponse(responseCode="400", description="Invalid input data"),
            @ApiResponse(responseCode="404", description="Lesson not found"),
            @ApiResponse(responseCode="401", description="Unauthorized")
    })
    @PutMapping("/{id}")
    public ResponseEntity<LessonResponseDTO> update(@PathVariable("id") UUID id,
                                                    @RequestBody @Valid LessonRequestDTO lesson) {
        return new ResponseEntity<>(service.update(id, lesson), HttpStatus.OK);
    }
}

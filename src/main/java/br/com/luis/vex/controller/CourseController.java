package br.com.luis.vex.controller;


import br.com.luis.vex.dto.course.CourseRequestDTO;
import br.com.luis.vex.dto.course.CourseResponseDTO;
import br.com.luis.vex.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/course")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService service;


    @PostMapping
    public ResponseEntity<CourseResponseDTO> create(@RequestBody CourseRequestDTO courseRequest) {
        return new ResponseEntity<>(service.create(courseRequest), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseResponseDTO> findById(@PathVariable("id") UUID id) {
        return new ResponseEntity<>(service.findById(id), HttpStatus.OK);

    }

    @GetMapping
    public ResponseEntity<List<CourseResponseDTO>> findAll() {
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }
}

package br.com.luis.vex.controller;


import br.com.luis.vex.dto.course.CourseRequestDTO;
import br.com.luis.vex.dto.course.CourseResponseDTO;
import br.com.luis.vex.dto.course.CourseUpdateDTO;
import br.com.luis.vex.model.enums.CategoryType;
import br.com.luis.vex.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/course")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService service;

    @PostMapping
    public ResponseEntity<CourseResponseDTO> create(@RequestBody @Valid CourseRequestDTO courseRequest) {
        return new ResponseEntity<>(service.create(courseRequest), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseResponseDTO> findById(@PathVariable("id") UUID id) {
        return new ResponseEntity<>(service.findById(id), HttpStatus.OK);

    }
    @GetMapping
    public ResponseEntity<Page<CourseResponseDTO>> findAllCoursesByFilter(  @PageableDefault(page = 0, size = 10) Pageable pageable,
                                                                            @RequestParam(name = "category", required = false) CategoryType category,
                                                                            @RequestParam(name = "title", required = false) String title) {
        return new ResponseEntity<>(service.findAllCoursesByFilter(category, title, pageable), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseResponseDTO> update(@PathVariable("id") UUID id,
                                                    @RequestBody @Valid CourseUpdateDTO courseUpdate) {
        return new ResponseEntity<>(service.update(id, courseUpdate), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable("id") UUID id) {
        service.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

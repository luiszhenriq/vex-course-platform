package br.com.luis.vex.controller;


import br.com.luis.vex.dto.course.CourseCustomResponse;
import br.com.luis.vex.dto.course.CourseRequestDTO;
import br.com.luis.vex.dto.course.CourseResponseDTO;
import br.com.luis.vex.dto.course.CourseUpdateDTO;
import br.com.luis.vex.model.enums.CategoryType;
import br.com.luis.vex.service.CoursePurchaseService;
import br.com.luis.vex.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    private final CoursePurchaseService coursePurchaseService;


    @Operation(summary = "Create course")
    @ApiResponses(value = {
            @ApiResponse(responseCode="201", description="Course created successfully"),
            @ApiResponse(responseCode="400", description="Invalid input data"),
            @ApiResponse(responseCode="401", description="Unauthorized"),
    })
    @PostMapping
    public ResponseEntity<CourseResponseDTO> create(@RequestBody @Valid CourseRequestDTO courseRequest) {
        return new ResponseEntity<>(service.create(courseRequest), HttpStatus.CREATED);
    }

    @Operation(summary = "Get course by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode="200", description="Course listed successfully"),
            @ApiResponse(responseCode="404", description="Course not found"),
            @ApiResponse(responseCode="401", description="Unauthorized")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CourseResponseDTO> findById(@PathVariable("id") UUID id) {
        return new ResponseEntity<>(service.findById(id), HttpStatus.OK);

    }

    @Operation(summary = "Get all courses")
    @ApiResponses(value = {
            @ApiResponse(responseCode="200", description="Return all courses successfully"),
            @ApiResponse(responseCode="401", description="Unauthorized")
    })
    @GetMapping
    public ResponseEntity<Page<CourseResponseDTO>> findAllCoursesByFilter(  @PageableDefault(page = 0, size = 10) Pageable pageable,
                                                                            @RequestParam(name = "category", required = false) CategoryType category,
                                                                            @RequestParam(name = "title", required = false) String title) {
        return new ResponseEntity<>(service.findAllCoursesByFilter(category, title, pageable), HttpStatus.OK);
    }

    @Operation(summary = "Get all courses purchase by user")
    @ApiResponses(value = {
            @ApiResponse(responseCode="200", description="Return all courses purchased successfully"),
            @ApiResponse(responseCode="401", description="Unauthorized")
    })
    @GetMapping("/my-courses")
    public ResponseEntity<Page<CourseCustomResponse>> findAllCoursesPurchaseByUser(@PageableDefault(page = 0, size = 10) Pageable pageable) {
        return new ResponseEntity<>(coursePurchaseService.findAllCoursesByUser(pageable), HttpStatus.OK);
    }


    @Operation(summary = "Update course")
    @ApiResponses(value = {
            @ApiResponse(responseCode="200", description="Course updated successfully"),
            @ApiResponse(responseCode="400", description="Invalid input data"),
            @ApiResponse(responseCode="404", description="Course not found"),
            @ApiResponse(responseCode="401", description="Unauthorized")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CourseResponseDTO> update(@PathVariable("id") UUID id,
                                                    @RequestBody @Valid CourseUpdateDTO courseUpdate) {
        return new ResponseEntity<>(service.update(id, courseUpdate), HttpStatus.OK);
    }

    @Operation(summary = "Delete course by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode="204", description="Course deleted successfully"),
            @ApiResponse(responseCode="401", description="Unauthorized")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable("id") UUID id) {
        service.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

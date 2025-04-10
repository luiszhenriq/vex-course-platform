package br.com.luis.vex.controller;

import br.com.luis.vex.dto.course.CourseRequestDTO;
import br.com.luis.vex.dto.course.CourseResponseDTO;
import br.com.luis.vex.dto.course.CourseUpdateDTO;
import br.com.luis.vex.dto.module.ModuleResponseDTO;
import br.com.luis.vex.model.Course;
import br.com.luis.vex.model.Module;
import br.com.luis.vex.model.enums.CategoryType;
import br.com.luis.vex.service.CourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CourseControllerTest {

    public static final UUID ID =                       UUID.randomUUID();
    public static final String TITLE =                  "CURSO DE JAVA";
    public static final String DESCRIPTION =            "FUNDAMENTOS BÁSICOS DE JAVA";
    public static final CategoryType CATEGORY_TYPE =    CategoryType.PROGRAMACAO;

    @InjectMocks
    private CourseController courseController;

    @Mock
    private CourseService courseService;

    private Course course;

    private CourseRequestDTO courseRequestDTO;

    private CourseUpdateDTO courseUpdateDTO;

    private CourseResponseDTO courseResponseDTO;

    private List<Module> modules = new ArrayList<>();

    private List<ModuleResponseDTO> moduleResponseDTOS = new ArrayList<>();

    private Pageable pageable;

    private Page<CourseResponseDTO> page;



    @BeforeEach
    void setUp() {
        course = new Course(ID, TITLE, DESCRIPTION, CATEGORY_TYPE, modules);
        courseRequestDTO = new CourseRequestDTO(TITLE, DESCRIPTION, CATEGORY_TYPE);
        courseResponseDTO = new CourseResponseDTO(ID, TITLE, DESCRIPTION, CATEGORY_TYPE, moduleResponseDTOS);
        courseUpdateDTO = new CourseUpdateDTO("CURSO DE DADOS", "EXCEL, BI", CategoryType.DADOS);

        pageable = PageRequest.of(0, 10);
        page = new PageImpl<>(List.of(courseResponseDTO));
    }

    @Test
    @DisplayName("Should return a created course with success")
    void shouldReturnACreatedCourseWithSuccess() {

        when(courseService.create(courseRequestDTO)).thenReturn(courseResponseDTO);

        ResponseEntity<CourseResponseDTO> response = courseController.create(courseRequestDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(courseResponseDTO, response.getBody());
        verify(courseService).create(courseRequestDTO);
    }

    @Test
    @DisplayName("Should return a course by id with success")
    void shouldReturnACourseByIdWithSuccess() {

        when(courseService.findById(ID)).thenReturn(courseResponseDTO);

        ResponseEntity<CourseResponseDTO> response = courseController.findById(ID);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(courseResponseDTO, response.getBody());
        verify(courseService).findById(ID);
    }

    @Test
    @DisplayName("Should return all courses by all filters")
    void ShouldReturnAllCoursesByFilters() {

        when(courseService.findAllCoursesByFilter(CATEGORY_TYPE, TITLE, pageable)).thenReturn(page);

        ResponseEntity<Page<CourseResponseDTO>> response = courseController.findAllCoursesByFilter(pageable, CATEGORY_TYPE, TITLE);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(page, response.getBody());
        verify(courseService).findAllCoursesByFilter(CATEGORY_TYPE, TITLE, pageable);
    }

    @Test
    @DisplayName("Should return all courses by filter title empty")
    void shouldReturnAllCoursesByFilterTitleEmpty() {

        when(courseService.findAllCoursesByFilter(CATEGORY_TYPE, "", pageable)).thenReturn(page);

        ResponseEntity<Page<CourseResponseDTO>> response = courseController.findAllCoursesByFilter(pageable, CATEGORY_TYPE, "");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(page, response.getBody());
        verify(courseService).findAllCoursesByFilter(CATEGORY_TYPE, "", pageable);
    }

    @Test
    @DisplayName("Should return all courses by filter category null")
    void shouldReturnAllCoursesByFilterCategoryNull() {

        when(courseService.findAllCoursesByFilter(null, TITLE, pageable)).thenReturn(page);

        ResponseEntity<Page<CourseResponseDTO>> response = courseController.findAllCoursesByFilter(pageable, null, TITLE);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(page, response.getBody());
        verify(courseService).findAllCoursesByFilter(null, TITLE, pageable);
    }

    @Test
    @DisplayName("Should return all courses by filter without category and title")
    void shouldReturnAllCoursesByFilterWithoutCategoryAndTitle() {

        when(courseService.findAllCoursesByFilter(CATEGORY_TYPE, TITLE, pageable)).thenReturn(page);

        ResponseEntity<Page<CourseResponseDTO>> response = courseController.findAllCoursesByFilter(pageable, CATEGORY_TYPE, TITLE);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(page, response.getBody());
        verify(courseService).findAllCoursesByFilter(CATEGORY_TYPE, TITLE, pageable);
    }

    @Test
    @DisplayName("Should update a course with success")
    void shouldUpdateACourseWithSuccess() {
        when(courseService.update(ID, courseUpdateDTO)).thenReturn(courseResponseDTO);

        ResponseEntity<CourseResponseDTO> response = courseController.update(ID, courseUpdateDTO);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(CourseResponseDTO.class, response.getBody().getClass());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Should delete a course with success")
    void shouldDeleteACourseWithSuccess() {
        doNothing().when(courseService).deleteById(ID);

        ResponseEntity<Void> response = courseController.deleteById(ID);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        verify(courseService, times(1)).deleteById(ID);
    }

}
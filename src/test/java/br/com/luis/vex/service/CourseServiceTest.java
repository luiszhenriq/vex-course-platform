package br.com.luis.vex.service;

import br.com.luis.vex.dto.course.CourseRequestDTO;
import br.com.luis.vex.dto.course.CourseResponseDTO;
import br.com.luis.vex.dto.course.CourseUpdateDTO;
import br.com.luis.vex.dto.user.UserResponseDTO;
import br.com.luis.vex.infra.exception.IdNotFoundException;
import br.com.luis.vex.model.Course;
import br.com.luis.vex.model.CoursePurchase;
import br.com.luis.vex.model.Module;
import br.com.luis.vex.model.enums.CategoryType;
import br.com.luis.vex.model.enums.UserType;
import br.com.luis.vex.repository.CoursePurchaseRepository;
import br.com.luis.vex.repository.CourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    public static final UUID ID =                       UUID.randomUUID();
    public static final String TITLE =                  "CURSO DE JAVA";
    public static final String DESCRIPTION =            "FUNDAMENTOS BÁSICOS DE JAVA";
    public static final CategoryType CATEGORY_TYPE =    CategoryType.PROGRAMACAO;

    @InjectMocks
    private CourseService courseService;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private UserService userService;

    @Mock
    private CoursePurchaseRepository coursePurchaseRepository;

    private Course course;

    private CourseRequestDTO courseRequestDTO;

    private CourseUpdateDTO courseUpdateDTO;

    private final List<Module> modules = new ArrayList<>();

    private UserResponseDTO user;

    @BeforeEach
    void setUp() {
        course = new Course(ID, TITLE, DESCRIPTION, CATEGORY_TYPE, modules);
        courseRequestDTO = new CourseRequestDTO(TITLE, DESCRIPTION, CATEGORY_TYPE);
        courseUpdateDTO = new CourseUpdateDTO("CURSO DE DADOS", "EXCEL, BI", CategoryType.DADOS);
        user = new UserResponseDTO(UUID.randomUUID(), "luis",
                "luis@gmail.com", "71981818282", "81158834511", UserType.ESTUDANTE);
    }


    @Test
    @DisplayName("Should return a created course with success")
    void shouldReturnACreatedCourseWithSuccess() {

        when(courseRepository.save(any())).thenReturn(course);

        CourseResponseDTO response = courseService.create(courseRequestDTO);

        assertNotNull(response);
        assertEquals(CourseResponseDTO.class, response.getClass());
        assertEquals(CATEGORY_TYPE, course.getCategory());
    }

    @Test
    @DisplayName("Should return a course by id with success")
    void shouldReturnACourseByIdWithSuccess() {

        CoursePurchase purchase = new CoursePurchase();

        when(courseRepository.findById(course.getId())).thenReturn(Optional.of(course));
        when(userService.getAuthenticatedUser()).thenReturn(user);
        when(coursePurchaseRepository.findByUserIdAndCourseId(user.id(), course.getId())).thenReturn(Optional.of(purchase));

        CourseResponseDTO response = courseService.findById(course.getId());

        assertNotNull(response);
        assertEquals(CourseResponseDTO.class, response.getClass());
        assertEquals(course.getId(), response.id());
    }

    @Test
    @DisplayName("Should return an id not found exception")
    void shouldReturnAnIdNotFoundException() {
        when(courseRepository.findById(course.getId())).thenThrow(new IdNotFoundException("Curso não encontrado"));

        try {
            courseService.findById(course.getId());
        }catch (Exception ex) {
            assertEquals(IdNotFoundException.class, ex.getClass());
            assertEquals("Curso não encontrado", ex.getMessage());
        }
    }

    @Test
    @DisplayName("Should return all courses with no filters")
    void shouldReturnAllCoursesWithNoFilters() {
        Pageable pageable = mock(Pageable.class);
        Page<Course> coursePage = new PageImpl<>(List.of(course), pageable, 1);

        when(courseRepository.findAllCoursesByFilter(isNull(), eq(""), eq(pageable)))
                .thenReturn(coursePage);

        Page<CourseResponseDTO> result = courseService.findAllCoursesByFilter(null, "", pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());

        verify(courseRepository).findAllCoursesByFilter(null, "", pageable);
    }

    @Test
    @DisplayName("Should return all courses by filters")
    void shouldReturnAllCoursesByFilters() {

        Pageable pageable = mock(Pageable.class);
        Page<Course> coursePage = new PageImpl<>(Collections.singletonList(course), pageable, 1);

        when(courseRepository.findAllCoursesByFilter(eq(CATEGORY_TYPE), eq(TITLE), eq(pageable)))
                .thenReturn(coursePage);

        Page<CourseResponseDTO> result = courseService.findAllCoursesByFilter(CATEGORY_TYPE, TITLE, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(CATEGORY_TYPE, result.getContent().get(0).category());

        verify(courseRepository).findAllCoursesByFilter(CATEGORY_TYPE, TITLE, pageable);
    }

    @Test
    @DisplayName("Should return all courses by filter ignore title filter")
    void shouldReturnAllCoursesByFilterIgnoreTitleFilter() {

        Pageable pageable = mock(Pageable.class);
        Page<Course> coursePage = new PageImpl<>(Collections.singletonList(course), pageable, 1);

        when(courseRepository.findAllCoursesByFilter(eq(CATEGORY_TYPE), eq(""), eq(pageable)))
                .thenReturn(coursePage);

        Page<CourseResponseDTO> result = courseService.findAllCoursesByFilter(CATEGORY_TYPE, "", pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());

        verify(courseRepository).findAllCoursesByFilter(CATEGORY_TYPE, "", pageable);
    }

    @Test
    @DisplayName("Should return all courses by filter ignore category filter")
    void shouldReturnAllCoursesByFilterIgnoreCategoryFilter() {

        Pageable pageable = mock(Pageable.class);
        Page<Course> coursePage = new PageImpl<>(Collections.singletonList(course), pageable, 1);

        when(courseRepository.findAllCoursesByFilter(isNull(), eq(TITLE), eq(pageable)))
                .thenReturn(coursePage);

        Page<CourseResponseDTO> result = courseService.findAllCoursesByFilter(null, TITLE, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());

        verify(courseRepository).findAllCoursesByFilter(null, TITLE, pageable);
    }

    @Test
    @DisplayName("Should update a course with success")
    void shouldUpdateACourseWithSuccess() {
        when(courseRepository.findById(ID)).thenReturn(Optional.of(course));
        when(courseRepository.save(any())).thenReturn(course);

        CourseResponseDTO response = courseService.update(ID, courseUpdateDTO);

        assertNotNull(response);
        assertEquals(CourseResponseDTO.class, response.getClass());
        assertEquals(ID, response.id());
        assertEquals(courseUpdateDTO.title(), response.title());
        assertEquals(courseUpdateDTO.description(), response.description());
        assertEquals(courseUpdateDTO.category(), response.category());

    }

    @Test
    @DisplayName("Should return an id not found exception when update course")
    void shouldReturnAnIdNotFoundExceptionWhenUpdateCourse() {

        when(courseRepository.findById(ID)).thenReturn(Optional.empty());

        Exception ex = assertThrows(IdNotFoundException.class, () -> {
            courseService.update(ID, courseUpdateDTO);
        });

        assertEquals("Curso não encontrado", ex.getMessage());
    }

    @Test
    @DisplayName("Should delete a course with success")
    void shouldDeleteACourseWithSuccess(){
        doNothing().when(courseRepository).deleteById(ID);

        courseService.deleteById(ID);

        verify(courseRepository, times(1)).deleteById(ID);
    }

}
package br.com.luis.vex.service;

import br.com.luis.vex.dto.course.CourseCustomResponse;
import br.com.luis.vex.model.Course;
import br.com.luis.vex.model.CoursePurchase;
import br.com.luis.vex.model.User;
import br.com.luis.vex.repository.CoursePurchaseRepository;
import br.com.luis.vex.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CoursePurchaseServiceTest {

    public static final UUID ID =                       UUID.randomUUID();
    public static final String TITLE =                  "CURSO DE JAVA";


    @InjectMocks
    private CoursePurchaseService coursePurchaseService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CoursePurchaseRepository coursePurchaseRepository;


    @Test
    void findAllCoursesByUser_ShouldReturnPageOfCourseCustomResponse() {

        Pageable pageable = PageRequest.of(0, 10);
        User authenticatedUser = new User();
        authenticatedUser.setEmail("user@example.com");

        CoursePurchase coursePurchase = new CoursePurchase();
        Course course = new Course();
        coursePurchase.setCourse(course);

        Page<CoursePurchase> mockPage = new PageImpl<>(Collections.singletonList(coursePurchase));

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("user@example.com");

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findUserByEmail("user@example.com")).thenReturn(authenticatedUser);
        when(coursePurchaseRepository.findAllByUser(authenticatedUser, pageable)).thenReturn(mockPage);

        Page<CourseCustomResponse> result = coursePurchaseService.findAllCoursesByUser(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());

        CourseCustomResponse response = result.getContent().get(0);
        assertNotNull(response);

        verify(userRepository).findUserByEmail("user@example.com");
        verify(coursePurchaseRepository).findAllByUser(authenticatedUser, pageable);
    }
}
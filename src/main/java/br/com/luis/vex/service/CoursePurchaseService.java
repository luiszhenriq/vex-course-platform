package br.com.luis.vex.service;


import br.com.luis.vex.dto.course.CourseCustomResponse;
import br.com.luis.vex.model.CoursePurchase;
import br.com.luis.vex.model.User;
import br.com.luis.vex.repository.CoursePurchaseRepository;
import br.com.luis.vex.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CoursePurchaseService {

    private final UserRepository userRepository;
    private final CoursePurchaseRepository repository;

    public Page<CourseCustomResponse> findAllCoursesByUser(Pageable pageable) {

        Page<CoursePurchase> courses = repository.findAllByUser(getAuthenticatedUser(), pageable);

        return courses.map(course -> new CourseCustomResponse(course.getCourse()));

    }


    private User getAuthenticatedUser() {

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String email = userDetails.getUsername();
        return this.userRepository.findUserByEmail(email);
    }


}

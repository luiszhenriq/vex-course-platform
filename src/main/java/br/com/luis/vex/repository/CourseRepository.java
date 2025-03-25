package br.com.luis.vex.repository;

import br.com.luis.vex.model.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface CourseRepository extends JpaRepository<Course, UUID> {

    @Query("SELECT e FROM Course e WHERE (:category IS NULL OR e.category = :category)")
    Page<Course> findAllCoursesByCategory(@Param("category") String category, Pageable pageable);

}

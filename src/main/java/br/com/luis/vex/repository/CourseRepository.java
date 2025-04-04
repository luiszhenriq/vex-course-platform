package br.com.luis.vex.repository;

import br.com.luis.vex.model.Course;
import br.com.luis.vex.model.enums.CategoryType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface CourseRepository extends JpaRepository<Course, UUID> {

    @Query("SELECT e FROM Course e WHERE " +
            "(:category IS NULL OR e.category = :category) AND " +
            "(COALESCE(:title, '') = '' OR LOWER(e.title) LIKE LOWER(CONCAT('%', :title, '%')))")
    Page<Course> findAllCoursesByFilter(
            @Param("category") CategoryType category,
            @Param("title") String title,
            Pageable pageable);
}

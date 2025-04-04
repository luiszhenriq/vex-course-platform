package br.com.luis.vex.repository;

import br.com.luis.vex.model.CoursePurchase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CoursePurchaseRepository extends JpaRepository<CoursePurchase, UUID> {

    Optional<CoursePurchase> findByUserIdAndCourseId(UUID userId, UUID courseId);
}

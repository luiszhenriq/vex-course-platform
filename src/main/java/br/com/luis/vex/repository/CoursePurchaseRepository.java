package br.com.luis.vex.repository;

import br.com.luis.vex.model.CoursePurchase;
import br.com.luis.vex.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CoursePurchaseRepository extends JpaRepository<CoursePurchase, UUID> {

    Optional<CoursePurchase> findByUserIdAndCourseId(UUID userId, UUID courseId);

    Page<CoursePurchase> findAllByUser(User user, Pageable pageable);
}

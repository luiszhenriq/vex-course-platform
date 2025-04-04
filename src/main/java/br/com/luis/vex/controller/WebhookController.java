package br.com.luis.vex.controller;


import br.com.luis.vex.dto.payment.PixPaymentDTO;
import br.com.luis.vex.model.Course;
import br.com.luis.vex.model.CoursePurchase;
import br.com.luis.vex.model.User;
import br.com.luis.vex.repository.CoursePurchaseRepository;
import br.com.luis.vex.repository.CourseRepository;
import br.com.luis.vex.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/webhook")
@RequiredArgsConstructor
public class WebhookController {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final CoursePurchaseRepository coursePurchaseRepository;


    @PostMapping
    public ResponseEntity<?> handlePixWebhook(@RequestBody PixPaymentDTO dto) {

        User user = userRepository.findUserByEmail(dto.data().billing().customer().metadata().email());

         dto.data().billing().products()
                .forEach(product -> {
                   Course course = courseRepository.findById(product.externalId()).orElseThrow();
                    CoursePurchase newPurchase = new CoursePurchase();
                    newPurchase.setUser(user);
                    newPurchase.setCourse(course);
                    newPurchase.setPurchaseDate(LocalDateTime.now());
                    newPurchase.setPaid(true);
                    coursePurchaseRepository.save(newPurchase);
                });

        return ResponseEntity.ok().build();
    }
}

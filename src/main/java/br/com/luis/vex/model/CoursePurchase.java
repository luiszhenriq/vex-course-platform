package br.com.luis.vex.model;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
public class CoursePurchase {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Course course;

    private LocalDateTime purchaseDate;

    private boolean paid;
}

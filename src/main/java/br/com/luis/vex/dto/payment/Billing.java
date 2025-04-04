package br.com.luis.vex.dto.payment;

import br.com.luis.vex.dto.course.CourseResponseDTO;

import java.util.List;

public record Billing(int amount, List<String>
        couponsUsed, Customer customer, String frequency, String id, List<String> kind, int paidAmount, List<Product> products, String status) {}

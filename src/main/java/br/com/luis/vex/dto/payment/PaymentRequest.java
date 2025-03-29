package br.com.luis.vex.dto.payment;

import br.com.luis.vex.dto.course.CourseResponseDTO;
import lombok.Data;

import java.util.List;


@Data
public class PaymentRequest {

    private List<CourseResponseDTO> products;

}

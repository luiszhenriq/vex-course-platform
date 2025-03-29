package br.com.luis.vex.controller;


import br.com.luis.vex.dto.payment.PaymentRequest;
import br.com.luis.vex.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;


    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> createBilling(@RequestBody PaymentRequest paymentRequest) {
        return paymentService.createBilling(paymentRequest);
    }

}
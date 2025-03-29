package br.com.luis.vex.service;


import br.com.luis.vex.dto.course.CourseResponseDTO;
import br.com.luis.vex.dto.payment.PaymentRequest;
import br.com.luis.vex.dto.user.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final String API_URL = "https://api.abacatepay.com/v1/billing/create";
    private final String secretKey = System.getenv("SECRET_KEY");

    private final UserService userService;
    private final CourseService courseService;


    public ResponseEntity<String> createBilling(PaymentRequest paymentRequest) {
        UserResponseDTO user = userService.getAuthenticatedUser();

        RestTemplate restTemplate = new RestTemplate();

        List<CourseResponseDTO> courses = paymentRequest.getProducts().stream()
                .map(course -> courseService.findById(course.id()))
                .toList();

        List<Map<String, Object>> products = courses.stream()
                .map(course -> {
                    Map<String, Object> productMap = new HashMap<>();
                    productMap.put("externalId", course.id().toString());
                    productMap.put("name", course.title());
                    productMap.put("quantity", 1);
                    productMap.put("price", 2990);
                    productMap.put("description", course.description());
                    return productMap;
                })
                .collect(Collectors.toList());

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("frequency", "ONE_TIME");
        requestBody.put("methods", List.of("PIX"));
        requestBody.put("returnUrl", "http://localhost:8080/failed");
        requestBody.put("completionUrl", "http://localhost:8080/success");

        Map<String, Object> customer = new HashMap<>();
        customer.put("name", user.fullName());
        customer.put("cellphone", user.cellphone());
        customer.put("email", user.email());
        customer.put("taxId", user.taxId());

        requestBody.put("customer", customer);
        requestBody.put("products", products);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", secretKey);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        return restTemplate.exchange(API_URL, HttpMethod.POST, requestEntity, String.class);
    }
}

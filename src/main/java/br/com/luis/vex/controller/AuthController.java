package br.com.luis.vex.controller;


import br.com.luis.vex.dto.user.UserLoginDTO;
import br.com.luis.vex.dto.user.UserRegisterDTO;
import br.com.luis.vex.dto.user.UserResponseDTO;
import br.com.luis.vex.infra.security.TokenJWT;
import br.com.luis.vex.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final UserService service;


    @Operation(summary = "User register")
    @ApiResponses(value = {
            @ApiResponse(responseCode="201", description = "Registered user"),
            @ApiResponse(responseCode="400", description = "Email already registered"),
            @ApiResponse(responseCode="404", description = "Invalid input data")
    })
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody @Valid UserRegisterDTO userRegister) {
        return new ResponseEntity<>(service.register(userRegister), HttpStatus.CREATED);
    }

    @Operation(summary = "User login")
    @ApiResponses(value = {
            @ApiResponse(responseCode="200", description = "Logged in user"),
            @ApiResponse(responseCode="401", description="Unauthorized"),
            @ApiResponse(responseCode="401", description="Invalid password"),
            @ApiResponse(responseCode="404", description="Email not found"),
            @ApiResponse(responseCode="404", description="Invalid input data")
    })
    @PostMapping("/login")
    public ResponseEntity<TokenJWT> login(@RequestBody @Valid UserLoginDTO userLogin) {
        String tokenJWT = service.login(userLogin);
        return ResponseEntity.ok(new TokenJWT(tokenJWT));
    }
}

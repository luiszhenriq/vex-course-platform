package br.com.luis.vex.controller;


import br.com.luis.vex.dto.user.UserLoginDTO;
import br.com.luis.vex.dto.user.UserRegisterDTO;
import br.com.luis.vex.dto.user.UserResponseDTO;
import br.com.luis.vex.infra.security.TokenJWT;
import br.com.luis.vex.service.UserService;
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


    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody UserRegisterDTO userRegister) {
        return new ResponseEntity<>(service.register(userRegister), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenJWT> login(@RequestBody UserLoginDTO userLogin) {
        String tokenJWT = service.login(userLogin);
        return ResponseEntity.ok(new TokenJWT(tokenJWT));
    }
}

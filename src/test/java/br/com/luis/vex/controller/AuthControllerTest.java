package br.com.luis.vex.controller;

import br.com.luis.vex.dto.user.UserLoginDTO;
import br.com.luis.vex.dto.user.UserRegisterDTO;
import br.com.luis.vex.dto.user.UserResponseDTO;
import br.com.luis.vex.infra.security.TokenJWT;
import br.com.luis.vex.model.enums.UserType;
import br.com.luis.vex.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    public static final UUID ID =                       UUID.randomUUID();
    public static final String FULL_NAME =              "Luis";
    public static final String EMAIL =                  "Luis@gmail.com";
    public static final String CELLPHONE =              "71981828283";
    public static final String TAX_ID =                 "01234567890";
    public static final String PASSWORD =               "senha123";
    public static final UserType USER_TYPE =            UserType.ESTUDANTE;

    @InjectMocks
    private AuthController authController;

    @Mock
    private UserService userService;

    private UserRegisterDTO userRegisterDTO;

    private UserLoginDTO userLoginDTO;


    private UserResponseDTO userResponseDTO;

    @BeforeEach
    void setUp() {
        userRegisterDTO = new UserRegisterDTO(FULL_NAME, EMAIL, CELLPHONE, TAX_ID, PASSWORD);
        userLoginDTO = new UserLoginDTO(EMAIL, PASSWORD);
        userResponseDTO = new UserResponseDTO(ID, FULL_NAME, EMAIL, CELLPHONE, TAX_ID, USER_TYPE);
    }

    @Test
    @DisplayName("Should return a user register with success")
    void shouldReturnAUserRegisterWithSuccess() {

        when(userService.register(userRegisterDTO)).thenReturn(userResponseDTO);

        ResponseEntity<UserResponseDTO> response = authController.register(userRegisterDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(userResponseDTO, response.getBody());
        assertEquals(FULL_NAME, response.getBody().fullName());
        assertEquals(EMAIL, response.getBody().email());
        verify(userService).register(userRegisterDTO);
    }

    @Test
    @DisplayName("Should return a token from user login")
    void shouldReturnATokenFromUserLogin() {

        String fakeToken = "fake-jwt-token";

        when(userService.login(userLoginDTO)).thenReturn(fakeToken);

        ResponseEntity<TokenJWT> response = authController.login(userLoginDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        verify(userService).login(userLoginDTO);
    }

}
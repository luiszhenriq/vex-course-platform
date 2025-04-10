package br.com.luis.vex.service;

import br.com.luis.vex.dto.user.UserLoginDTO;
import br.com.luis.vex.dto.user.UserRegisterDTO;
import br.com.luis.vex.dto.user.UserResponseDTO;
import br.com.luis.vex.infra.exception.EmailAlreadyRegisteredException;
import br.com.luis.vex.infra.exception.EmailNotFoundException;
import br.com.luis.vex.infra.exception.InvalidPasswordException;
import br.com.luis.vex.infra.security.TokenService;
import br.com.luis.vex.model.User;
import br.com.luis.vex.model.enums.UserType;
import br.com.luis.vex.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    public static final UUID ID =                       UUID.randomUUID();
    public static final String FULL_NAME =              "Luis";
    public static final String EMAIL =                  "Luis@gmail.com";
    public static final String CELLPHONE =              "71981828283";
    public static final String TAX_ID =                 "01234567890";
    public static final String PASSWORD =               "senha123";
    public static final UserType USER_TYPE =            UserType.ESTUDANTE;


    @Mock
    private UserRepository repository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager manager;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private UserService userService;

    private UserRegisterDTO userRegisterDTO;

    private UserLoginDTO userLoginDTO;

    private User user;

    @BeforeEach
    void setUp() {
        userRegisterDTO = new UserRegisterDTO(FULL_NAME, EMAIL, CELLPHONE, TAX_ID, PASSWORD);
        userLoginDTO = new UserLoginDTO(EMAIL, PASSWORD);
        user = new User(ID, FULL_NAME, EMAIL, CELLPHONE, TAX_ID, PASSWORD, USER_TYPE);
    }

    @Test
    @DisplayName("Should return a user register with success")
    void shouldReturnAUserRegisterWithSuccess() {
        when(repository.findByEmail(EMAIL)).thenReturn(null);

        String encryptedPassword = "$2a$10$N9qo8uLOickgx2ZMRZoMy.Mrq4L0LziF0YjZ4Lr3q7n7JY8fH6K0W";
        when(passwordEncoder.encode(PASSWORD)).thenReturn(encryptedPassword);

        User savedUser = new User();
        savedUser.setPassword(encryptedPassword);
        when(repository.save(any(User.class))).thenReturn(savedUser);

        UserResponseDTO result = userService.register(userRegisterDTO);

        assertNotNull(result);

        verify(passwordEncoder).encode(PASSWORD);

        verify(repository).save(argThat(user ->
                user.getPassword().equals(encryptedPassword)
        ));
    }

    @Test
    @DisplayName("Should return an email already registered exception when user register")
    void shouldReturnAnEmailAlreadyRegisteredExceptionWhenUserRegister() {

        User existingUser = new User();
        existingUser.setEmail(EMAIL);

        when(repository.findByEmail(EMAIL)).thenReturn(existingUser);

        EmailAlreadyRegisteredException exception = assertThrows(
                EmailAlreadyRegisteredException.class,
                () -> userService.register(userRegisterDTO)
        );

        assertEquals("Este email já está cadastrado", exception.getMessage());
        verify(repository).findByEmail(EMAIL);
        verify(repository, never()).save(any(User.class));
        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    @DisplayName("Should return a token from the user login ")
    void shouldReturnATokenFromTheUserLogin() {

        when(repository.findByEmail(userLoginDTO.email())).thenReturn(user);
        when(passwordEncoder.matches(userLoginDTO.password(), user.getPassword())).thenReturn(true);

        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn(user);
        when(manager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);

        when(tokenService.generateToken(user)).thenReturn("api.security.token.secret");

        String result = userService.login(userLoginDTO);

        assertEquals("api.security.token.secret", result);
        verify(repository).findByEmail(EMAIL);
        verify(passwordEncoder).matches(PASSWORD, user.getPassword());
        verify(manager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenService).generateToken(user);
    }

    @Test
    @DisplayName("Should return email not found exception when email not exist ")
    void shouldReturnEmailNotFoundExceptionWhenEmailNotExist() {

        when(repository.findByEmail(userLoginDTO.email())).thenReturn(null);

        EmailNotFoundException exception = assertThrows(
                EmailNotFoundException.class,
                () -> userService.login(userLoginDTO)
        );

        assertEquals("Email não encontrado", exception.getMessage());

        verify(repository).findByEmail(EMAIL);
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(manager, never()).authenticate(any());
        verify(tokenService, never()).generateToken(any());
    }

    @Test
    @DisplayName("Should return invalid password when password wrong")
    void shouldReturnInvalidPasswordWhenPasswordWrong() {

        when(repository.findByEmail(EMAIL)).thenReturn(user);
        when(passwordEncoder.matches(PASSWORD, user.getPassword())).thenReturn(false);

        InvalidPasswordException exception = assertThrows(
                InvalidPasswordException.class,
                () -> userService.login(userLoginDTO)
        );

        assertEquals("Senha inválida", exception.getMessage());
        verify(repository).findByEmail(EMAIL);
        verify(passwordEncoder).matches(PASSWORD, user.getPassword());
        verify(manager, never()).authenticate(any());
        verify(tokenService, never()).generateToken(any());
    }
}
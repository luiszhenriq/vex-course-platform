package br.com.luis.vex.service;


import br.com.luis.vex.dto.user.UserLoginDTO;
import br.com.luis.vex.dto.user.UserRegisterDTO;
import br.com.luis.vex.dto.user.UserResponseDTO;
import br.com.luis.vex.infra.exception.EmailAlreadyRegisteredException;
import br.com.luis.vex.infra.exception.InvalidPasswordException;
import br.com.luis.vex.infra.security.TokenService;
import br.com.luis.vex.model.User;
import br.com.luis.vex.model.enums.UserType;
import br.com.luis.vex.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final AuthenticationManager manager;
    private final TokenService tokenService;
    private final BCryptPasswordEncoder passwordEncoder;


    public UserResponseDTO register(UserRegisterDTO userRegister) {

        if (this.repository.findByEmail(userRegister.email()) != null) {
            throw new EmailAlreadyRegisteredException("Este email já está cadastrado");
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(userRegister.password());

        User newUser = new User(userRegister);

        newUser.setPassword(encryptedPassword);
        newUser.setUserType(UserType.ESTUDANTE);

        User savedUser = repository.save(newUser);

        return userResponseDTO(savedUser);
    }

    public String login(UserLoginDTO userLogin) {

        User user = (User) repository.findByEmail(userLogin.email());

        if (!this.passwordEncoder.matches(userLogin.password(), user.getPassword())) {
            throw new InvalidPasswordException("Senha inválida");
        }

        var token = new UsernamePasswordAuthenticationToken(userLogin.email(), userLogin.password());
        var auth = manager.authenticate(token);

        return tokenService.generateToken((User) auth.getPrincipal());
    }

    private UserResponseDTO userResponseDTO(User user) {

        return new UserResponseDTO(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getUserType()
        );
    }
}

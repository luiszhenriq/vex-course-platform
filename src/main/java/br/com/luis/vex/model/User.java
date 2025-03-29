package br.com.luis.vex.model;


import br.com.luis.vex.dto.user.UserRegisterDTO;
import br.com.luis.vex.model.enums.UserType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String fullName;

    @Column(unique = true)
    private String email;

    private String cellphone;

    private String taxId;

    private String password;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    public User(UserRegisterDTO userRegister) {
        this.fullName = userRegister.fullName();
        this.email = userRegister.email();
        this.cellphone = userRegister.cellphone();
        this.taxId = userRegister.taxId();
        this.password = userRegister.password();
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (UserType.INSTRUTOR.equals(this.userType)) {
            return List.of(new SimpleGrantedAuthority("ROLE_INSTRUTOR"));
        }
        return List.of(new SimpleGrantedAuthority("ROLE_ESTUDANTE"));
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

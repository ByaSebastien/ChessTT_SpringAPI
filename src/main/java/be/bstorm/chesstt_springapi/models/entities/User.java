package be.bstorm.chesstt_springapi.models.entities;


import be.bstorm.chesstt_springapi.models.enums.UserGender;
import be.bstorm.chesstt_springapi.models.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.validator.constraints.Range;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@ToString(of = {"id", "username", "email"})
@EqualsAndHashCode(of = {"id", "username", "email"})
@Entity
@Table(name = "USER_")
public class User implements UserDetails {

    @Getter
    @Setter
    @Column(name = "ID_USER")
    @Id
    private UUID id;

    @Getter
    @Setter
    @Column(name = "USERNAME", nullable = false, unique = true, length = 50)
    private String username;

    @Getter
    @Setter
    @Column(name = "EMAIL", nullable = false, unique = true, length = 100)
    private String email;

    @Getter
    @Setter
    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Getter
    @Setter
    @Column(name = "BIRTH_DATE", nullable = false)
    @Temporal(TemporalType.DATE)
    private LocalDate birthdate;

    @Getter
    @Setter
    @Column(name = "ELO", nullable = false)
    @Range(min = 0, max = 3000)
    private Integer elo;

    @Getter
    @Setter
    @Column(name = "GENDER", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserGender gender;

    @Getter
    @Setter
    @Column(name = "ROLE", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    public User(String username, String email, LocalDate birthdate, Integer elo, UserGender gender, UserRole role) {
        this.username = username;
        this.email = email;
        this.birthdate = birthdate;
        this.elo = elo;
        this.gender = gender;
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.role.toString()));
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

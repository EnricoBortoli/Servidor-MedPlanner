package medplanner.model;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "idUsuario")
@Inheritance(strategy = InheritanceType.JOINED)
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;

    @Column()
    @Email(message = "Email inválido")
    @Size(max = 50, message = "O nome de usuário deve ter no máximo 50 caracteres")
    private String username;

    @Column()
    private String password;

    @Column()
    @Nonnull()
    @NotEmpty(message = "O nome é obrigatório")
    @Size(min = 2, message = "O nome precisa ter mais de 2 caracteres")
    @Size(max = 200, message = "O nome deve ter no máximo 200 caracteres")
    private String nome;

    @Column()
    @NotNull(message = "O CPF é obrigatório")
    @NotEmpty(message = "O CPF é obrigatório")
    @Size(min = 11, max = 11, message = "O CPF deve ter exatamente 11 caracteres")
    private String cpf;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Cargo cargo;

    /**
     * A - Ativo
     * I - Inativo
     * E - Em validação (para quando esta com a senha temporaria)
     */
    @Column()
    @NotEmpty(message = "A situação é obrigatória")
    @Size(max = 1, message = "A situação deve ter no máximo 1 caractere")
    private String situacao;

    public enum Cargo {
        ADMINISTRADOR,
        RECEPCAO,
        MEDICO
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    /*
     * Esse método retorna apenas true porque não existe validação de usuário
     */
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    /*
     * Esse método retorna apenas true porque não existe validação de usuário
     */
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    /*
     * Esse método retorna apenas true porque não existe validação de usuário
     */
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    /*
     * Esse método retorna apenas true porque não existe validação de usuário ativo
     * ou inativo
     */
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(cargo.name()));
    }
}
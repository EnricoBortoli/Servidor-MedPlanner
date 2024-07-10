package medplanner.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;

import medplanner.model.Locacao;
import medplanner.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @Query(value = "SELECT * FROM Usuario p WHERE CAST(p.id_Usuario AS char) LIKE ?1%", nativeQuery = true)
    List<Usuario> findAllByIdUsuario(String id);

    @Query(value = "SELECT * FROM Usuario p WHERE p.nome LIKE ?1%", nativeQuery = true)
    List<Usuario> findAllByNomeUsuario(String nome);

    //@Query(value = "SELECT * FROM usuario p WHERE p.cpf = ?1", nativeQuery = true)
    //Usuario findByCPF(String cpf);

    @Query("SELECT u FROM Usuario u WHERE u.cpf = :cpf")
    Usuario findByCPF(String cpf);

    @Query(value = "select *,  case when prof.id_usuario is not null then 1 else 0 end as clazz_ from usuario left join profissional prof on usuario.id_usuario=prof.id_usuario where usuario.username = ?1",nativeQuery=true)
    Optional<Usuario> buscarPorEmail(String email);

    UserDetails findByUsername(String username);

    Optional<Usuario> findById(Long id);
}

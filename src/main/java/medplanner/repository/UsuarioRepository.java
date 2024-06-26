package medplanner.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;

import medplanner.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @Query(value = "SELECT * FROM Usuario p WHERE CAST(p.id_Usuario AS char) LIKE ?1%", nativeQuery = true)
    List<Usuario> findAllByIdUsuario(String id);

    @Query(value = "SELECT * FROM Usuario p WHERE p.nome LIKE ?1%", nativeQuery = true)
    List<Usuario> findAllByNomeUsuario(String nome);

    @Query(value = "SELECT * FROM usuario p WHERE p.cpf = ?1", nativeQuery = true)
    Usuario findByCPF(String cpf);

    UserDetails findByUsername(String username);

    Optional<Usuario> findById(Long id);
}

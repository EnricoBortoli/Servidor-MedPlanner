package medplanner.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import medplanner.model.Usuario;
public interface UsuarioRepository extends JpaRepository<Usuario, Long>  {
    
}

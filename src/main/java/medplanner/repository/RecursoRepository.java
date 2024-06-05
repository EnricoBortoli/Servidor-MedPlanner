package medplanner.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import medplanner.model.Recurso;

public interface RecursoRepository extends JpaRepository<Recurso, Long> {

    @Query(value = "SELECT * FROM Recurso p WHERE CAST(p.idRecurso AS char) LIKE ?1%", nativeQuery = true)
    List<Recurso> findAllByIdRecurso(String id);

    @Query(value = "SELECT * FROM Sala p WHERE p.nome LIKE ?1%", nativeQuery = true)
    List<Recurso> findByNomeRecurso(String nomeRecurso);
    
}

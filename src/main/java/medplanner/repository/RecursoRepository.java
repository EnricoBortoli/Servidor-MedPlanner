package medplanner.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import medplanner.model.Recurso;

public interface RecursoRepository extends JpaRepository<Recurso, Long> {

    @Query(value = "SELECT * FROM Recursos p WHERE p.id_recurso = ?1", nativeQuery = true)
    List<Recurso> findAllByIdRecurso(Long id);

    @Query(value = "SELECT * FROM Recursos p WHERE p.nome_recurso LIKE ?1", nativeQuery = true)
    List<Recurso> findByNomeRecurso(String nomeRecurso);

    @Query(value = "SELECT * FROM Recursos p WHERE p.id_sala = ?1", nativeQuery = true)
    List<Recurso> findByIdSala(Long idSala);

}

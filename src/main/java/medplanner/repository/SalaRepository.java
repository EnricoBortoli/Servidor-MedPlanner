package medplanner.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import medplanner.model.Sala;

public interface SalaRepository extends JpaRepository<Sala, Long> {

    @Query(value = "SELECT * FROM Sala p WHERE CAST(p.idSala AS char) LIKE ?1%", nativeQuery = true)
    List<Sala> findAllByIdSala(String id);

    @Query(value = "SELECT * FROM Sala p WHERE p.nome LIKE ?1%", nativeQuery = true)
    List<Sala> findByNomeSala(String nomeSala);

    @Query(value = "SELECT * FROM Sala p WHERE p.situacao LIKE ?1%", nativeQuery = true)
    List<Sala> findAllBySituacao(String situacao);
    
}

package medplanner.repository;

import medplanner.model.Ala;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AlaRepository extends JpaRepository<Ala, Long> {

    Optional<Ala> findByNome(String nome);
    Optional<Ala> findBySigla(String sigla);
    List<Ala> findAllByOrderByNome();
}

package medplanner.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import medplanner.model.Especialidade;

public interface EspecialidadeRepository extends JpaRepository<Especialidade, Long> {

    @Query(value = "SELECT * FROM Especialidade p WHERE CAST(p.id_Especialidade AS char) LIKE ?1%", nativeQuery = true)
    List<Especialidade> findAllByIdEspecialidade(String id);

    List<Especialidade> findByNomeStartingWith(String nome);

    List<Especialidade> findBySiglaStartingWith(String sigla);


}

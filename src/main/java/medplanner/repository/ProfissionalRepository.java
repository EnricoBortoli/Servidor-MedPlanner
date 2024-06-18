package medplanner.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import medplanner.model.Profissional;
import medplanner.model.Usuario;

public interface ProfissionalRepository extends JpaRepository<Profissional, Long> {

    @Query(value = "SELECT * FROM Profissional p WHERE CAST(p.id_Usuario AS char)LIKE ?1%", nativeQuery = true)
   
    List<Profissional> findByNumCrmStartingWith(String numCrm);

    List<Profissional> findByUfCrmStartingWith(String ufCrm);

    @Query("SELECT p FROM Profissional p WHERE p.especialidade.nome = ?1")
    List<Profissional> findByEspecialidadeNome(String especialidadeNome);

    @Query("SELECT p FROM Profissional p WHERE p.especialidade.sigla = ?1")
    List<Profissional> findByEspecialidadeSigla(String especialidadeSigla);

    @Query("SELECT p FROM Profissional p WHERE p.numCrm = ?1")
    Profissional findByCRM(String numCrm);

}

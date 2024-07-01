package medplanner.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import medplanner.model.Sala;

public interface SalaRepository extends JpaRepository<Sala, Long> {

    @Query(value = "SELECT * FROM Sala p WHERE p.id_sala = ?1", nativeQuery = true)
    List<Sala> findAllByIdSala(Long id);

    @Query(value = "SELECT * FROM Sala p WHERE p.nome_sala LIKE ?1", nativeQuery = true)
    List<Sala> findByNomeSala(String nomeSala);

    @Query(value = "SELECT * FROM Sala p WHERE p.situacao LIKE ?1%", nativeQuery = true)
    List<Sala> findAllBySituacao(String situacao);

    @Query(value = "SELECT * FROM Sala p WHERE p.id_ala = ?1", nativeQuery = true)
    List<Sala> findByAla(Long idAla);

    @Query(value = "SELECT * FROM Sala p WHERE p.andar = ?1", nativeQuery = true)
    List<Sala> findByAndar(Integer andar);

    @Query(value = "SELECT * FROM Sala p WHERE p.id_ala = ?1 AND p.situacao = ?2", nativeQuery = true)
    List<Sala> findByAlaAndSituacao(Long idAla, String situacao);

}

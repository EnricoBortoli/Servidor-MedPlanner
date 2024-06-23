package medplanner.repository;

import medplanner.model.Locacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LocacaoRepository extends JpaRepository<Locacao, Long> {

    @Query("SELECT l FROM Locacao l WHERE l.sala.id = :salaId AND l.dataInicio >= :dataInicio AND l.dataFim <= :dataFim")
    List<Locacao> findBySalaIdAndPeriodo(Long salaId, LocalDateTime dataInicio, LocalDateTime dataFim);

    @Query("SELECT l FROM Locacao l WHERE l.medico.id = :medicoId AND l.dataInicio >= :dataInicio AND l.dataFim <= :dataFim")
    List<Locacao> findByMedicoIdAndPeriodo(Long medicoId, LocalDateTime dataInicio, LocalDateTime dataFim);
}

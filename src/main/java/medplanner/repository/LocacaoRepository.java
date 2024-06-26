package medplanner.repository;

import medplanner.dto.SalaDTO;
import medplanner.model.Locacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Date;

@Repository
public interface LocacaoRepository extends JpaRepository<Locacao, Long> {

    @Query("select 1 from Locacao l where l.horaInicio <= :horaFinal and l.horaFinal >= :horaInicio and l.sala = :sala and l.data = :data")
    boolean existeDataHoraMarcadaNaSala(SalaDTO sala, LocalDateTime horaInicio, LocalDateTime horaFinal, Date data);

    @Query("select 1 from Locacao l where l.horaInicio <= :horaFinal and l.horaFinal >= :horaInicio and l.sala = :sala and l.idLocacao = :idLocacao and l.data = :data")
    boolean existeDataHoraMarcadaNaSala(Long idLocacao, SalaDTO sala, LocalDateTime horaInicio, LocalDateTime horaFinal, Date data);

//    @Query("SELECT l FROM Locacao l WHERE l.sala.id = :salaId AND l.dataInicio >= :dataInicio AND l.dataFim <= :dataFim")
//    List<Locacao> findBySalaIdAndPeriodo(Long salaId, LocalDateTime dataInicio, LocalDateTime dataFim);
//
//    @Query("SELECT l FROM Locacao l WHERE l.medico.id = :medicoId AND l.dataInicio >= :dataInicio AND l.dataFim <= :dataFim")
//    List<Locacao> findByMedicoIdAndPeriodo(Long medicoId, LocalDateTime dataInicio, LocalDateTime dataFim);
}

package medplanner.repository;

import medplanner.model.Locacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface LocacaoRepository extends JpaRepository<Locacao, Long> {

    @Query("select 1 from Locacao l where and l.horaInicio <= :horaFinal and l.horaFinal >= :horaInicio and l.sala = :sala and l.data = :data")
    boolean existeDataHoraMarcadaNaSala(Sala sala, LocalDateTime horaInicio, LocalDateTime horaFinal, Date data);

    @Query("select 1 from Locacao l where and l.horaInicio <= :horaFinal and l.horaFinal >= :horaInicio and l.sala = :sala and l.idLocacao = :idLocacao and l.data = :data")
    boolean existeDataHoraMarcadaNaSala(Long idLocacao, Sala sala, LocalDateTime horaInicio, LocalDateTime horaFinal, Date data);

}

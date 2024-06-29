package medplanner.repository;

import medplanner.model.Locacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Date;

@Repository
public interface LocacaoRepository extends JpaRepository<Locacao, Long> {

    @Query("select 1 from Locacao l where l.horaInicio <= :horaFinal and l.horaFinal >= :horaInicio and l.sala = :idsala and l.dia = :data")
    boolean existeDataHoraMarcadaNaSala(Long idsala, LocalDateTime horaInicio, LocalDateTime horaFinal, Date data);

    @Query("SELECT l FROM Locacao l WHERE l.sala.idSala = :salaId AND l.horaInicio >= :dataInicio AND l.horaFinal <= :dataFim")
    List<Locacao> findBySalaIdAndPeriodo(Long salaId, LocalDateTime dataInicio, LocalDateTime dataFim);

    @Query("SELECT l FROM Locacao l WHERE l.usuario.idUsuario = :medicoId AND l.horaInicio >= :dataInicio AND l.horaFinal <= :dataFim")
    List<Locacao> findByMedicoIdAndPeriodo(Long medicoId, LocalDateTime dataInicio, LocalDateTime dataFim);

    @Query(value = "SELECT * FROM Locacao l WHERE CAST(l.id_usuario AS char) LIKE ?1%", nativeQuery = true)
    List<Locacao> findByMedico(String idMedico);

    @Query(value = "SELECT * FROM Locacao l WHERE CAST(l.id_sala AS char) LIKE ?1%", nativeQuery = true)
    List<Locacao> findBySala(String idSala);

}

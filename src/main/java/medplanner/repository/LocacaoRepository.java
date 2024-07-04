package medplanner.repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import medplanner.model.Locacao;

@Repository
public interface LocacaoRepository extends JpaRepository<Locacao, Long> {

    @Query("select count(l) > 0 from Locacao l where l.horaInicio < :horaFinal and l.horaFinal > :horaInicio and l.sala.id = :idsala and l.dia = :dia")
    boolean existeDataHoraMarcadaNaSala(Long idsala, LocalDateTime horaInicio, LocalDateTime horaFinal, Date dia);

    @Query("select count(l) > 0 from Locacao l where l.id != :idAtual and l.horaInicio < :horaFinal and l.horaFinal > :horaInicio and l.sala.id = :idsala and l.dia = :dia")
    boolean existeDataHoraMarcadaNaSalaEditar(Long idAtual, Long idsala, LocalDateTime horaInicio,
            LocalDateTime horaFinal,
            Date dia);

    @Query("SELECT l FROM Locacao l WHERE l.sala.idSala = :salaId AND l.horaInicio >= :dataInicio AND l.horaFinal <= :dataFim")
    List<Locacao> findBySalaIdAndPeriodo(Long salaId, LocalDateTime dataInicio, LocalDateTime dataFim);

    @Query("SELECT l FROM Locacao l WHERE l.usuario.idUsuario = :medicoId AND l.horaInicio >= :dataInicio AND l.horaFinal <= :dataFim")
    List<Locacao> findByMedicoIdAndPeriodo(Long medicoId, LocalDateTime dataInicio, LocalDateTime dataFim);

    @Query(value = "SELECT * FROM Locacao l WHERE CAST(l.id_usuario AS char) LIKE ?1%", nativeQuery = true)
    List<Locacao> findByMedico(String idMedico);

    @Query(value = "SELECT * FROM Locacao l WHERE CAST(l.id_sala AS char) LIKE ?1%", nativeQuery = true)
    List<Locacao> findBySala(String idSala);

    @Query("SELECT l FROM Locacao l WHERE l.dia = :dia")
    List<Locacao> findByData(Date dia);
}

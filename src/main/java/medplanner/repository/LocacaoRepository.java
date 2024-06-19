package medplanner.repository;

import medplanner.model.Locacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface LocacaoRepository extends JpaRepository<Locacao, Long> {
    boolean existeDataHoraMarcadaNaSala(Date data, Date horaInicio, Date horaFinal); //, Sala sala
    boolean existsByDataAndHoraInicioAfter(Date date);
}

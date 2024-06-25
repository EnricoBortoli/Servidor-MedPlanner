package medplanner.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import medplanner.model.Recurso;
import medplanner.model.Sala;
import medplanner.repository.RecursoRepository;
import medplanner.repository.SalaRepository;

@Service
public class RecursoService {

  @Autowired
  private RecursoRepository recursoRepository;

  @Autowired
  private SalaRepository salaRepository;

  public List<Recurso> buscarTodosRecursos() {
    return recursoRepository.findAll();
  }

  public Optional<Recurso> buscarRecursoPorId(Long idRecurso) {
    return recursoRepository.findById(idRecurso);
  }

  public List<Recurso> buscarRecursosPorNome(String nomeRecurso) {
    String nomeRecursoComCuringa = "%" + nomeRecurso + "%";
    return recursoRepository.findByNomeRecurso(nomeRecursoComCuringa);
  }

  public List<Recurso> buscarRecursosPorIdSala(Long idSala) {
    return recursoRepository.findByIdSala(idSala);
  }

  public Recurso salvarRecurso(Recurso recurso) {
    return recursoRepository.save(recurso);
  }

  public Recurso salvarRecursoComSala(Recurso recurso, Long idSala) throws DataIntegrityViolationException {
    Optional<Sala> salaOpt = salaRepository.findById(idSala);
    if (salaOpt.isPresent()) {
      recurso.setSala(salaOpt.get());
      return recursoRepository.save(recurso);
    } else {
      throw new DataIntegrityViolationException("Sala não encontrada para o ID fornecido.");
    }
  }

  public void deletarRecurso(Long idRecurso) {
    recursoRepository.deleteById(idRecurso);
  }
}

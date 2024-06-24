package medplanner.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import medplanner.model.Recurso;
import medplanner.model.Sala;
import medplanner.repository.RecursoRepository;
import medplanner.repository.SalaRepository;

@Service
public class SalaService {

  @Autowired
  private SalaRepository salaRepository;

  @Autowired
  private RecursoRepository recursoRepository;

  public List<Sala> buscarTodasSalas() {
    return salaRepository.findAll();
  }

  public Optional<Sala> buscarSalaPorId(Long idSala) {
    return salaRepository.findById(idSala);
  }

  public List<Sala> buscarSalasPorNome(String nomeSala) {
    String nomeSalaComCuringa = "%" + nomeSala + "%";
    return salaRepository.findByNomeSala(nomeSalaComCuringa);
  }

  public List<Sala> buscarSalasPorSituacao(String situacao) {
    return salaRepository.findAllBySituacao(situacao);
  }

  public Sala salvarSala(Sala sala) {
    // Salva a sala sem os recursos associados
    Sala salaSalva = salaRepository.save(sala);
    // Atualiza os recursos com o id da sala salva
    if (sala.getRecursos() != null) {
      for (Recurso recurso : sala.getRecursos()) {
        recurso.setSala(salaSalva); // Associa a sala salva ao recurso
        recursoRepository.save(recurso); // Salva o recurso com o id da sala atualizado
      }
    }
    // Retorna a sala salva com os recursos atualizados
    salaSalva.setRecursos(sala.getRecursos());
    return salaSalva;
  }

  public void deletarSala(Long idSala) {
    salaRepository.deleteById(idSala);
  }
}

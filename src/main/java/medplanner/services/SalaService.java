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

  public List<Sala> buscarSalasPorAla(Long idAla) {
    return salaRepository.findByAla(idAla);
  }

  public List<Sala> buscarSalasPorAlaESituacao(Long idAla, String situacao) {
    return salaRepository.findByAlaAndSituacao(idAla, situacao);
  }

  public List<Sala> buscarSalasPorAndar(Integer andar) {
    return salaRepository.findByAndar(andar);
  }

  public Sala salvarSala(Sala sala) {

    Sala salaSalva = salaRepository.save(sala);

    if (sala.getRecursos() != null) {
      for (Recurso recurso : sala.getRecursos()) {
        recurso.setSala(salaSalva);
        recursoRepository.save(recurso);
      }
    }

    salaSalva.setRecursos(sala.getRecursos());
    return salaSalva;
  }

  public void deletarSala(Long idSala) {
    salaRepository.deleteById(idSala);
  }
}

package medplanner.services;

import medplanner.exception.ResourceNotFoundException;
import medplanner.model.Ala;
import medplanner.repository.AlaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AlaService {

    @Autowired
    private AlaRepository alaRepository;

    public Ala createAla(Ala ala) {
        return alaRepository.save(ala);
    }

    public List<Ala> getAllAlas() {
        return alaRepository.findAll();
    }

    public Optional<Ala> getAlaById(Long id) {
        return alaRepository.findById(id);
    }

    public Ala updateAla(Long id, Ala alaDetails) {
        Ala ala = alaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Ala não encontrada pelo id : " + id));
        ala.setNome(alaDetails.getNome());
        ala.setSigla(alaDetails.getSigla());
        ala.setAndar(alaDetails.getAndar());
        return alaRepository.save(ala);
    }

    public void deleteAla(Long id) {
        Ala ala = alaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Ala não encontrada pelo id : " + id));
        alaRepository.delete(ala);
    }
}

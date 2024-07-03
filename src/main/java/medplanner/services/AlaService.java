package medplanner.services;

import medplanner.model.Ala;
import medplanner.repository.AlaRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AlaService {

    @Autowired
    private AlaRepository alaRepository;

    public List<String> validateAla(Ala alaDetails, BindingResult bindingResult) {
        List<String> errors = bindingResult.getAllErrors().stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.toList());

        if (alaDetails.getIdAla() == null) {
            Optional<Ala> existingAlaByNome = alaRepository.findByNome(alaDetails.getNome());
            if (existingAlaByNome.isPresent()) {
                errors.add("Esse nome já existe");
            }

            Optional<Ala> existingAlaBySigla = alaRepository.findBySigla(alaDetails.getSigla());
            if (existingAlaBySigla.isPresent()) {
                errors.add("Essa sigla já existe");
            }
        } else {
            Optional<Ala> existingAlaByNome = alaRepository.findByNome(alaDetails.getNome());
            if (existingAlaByNome.isPresent() && !existingAlaByNome.get().getIdAla().equals(alaDetails.getIdAla())) {
                errors.add("Esse nome já existe");
            }

            Optional<Ala> existingAlaBySigla = alaRepository.findBySigla(alaDetails.getSigla());
            if (existingAlaBySigla.isPresent() && !existingAlaBySigla.get().getIdAla().equals(alaDetails.getIdAla())) {
                errors.add("Essa sigla já existe");
            }
        }

        return errors;
    }

    public Ala saveAla(Ala alaDetails) {
        return alaRepository.save(alaDetails);
    }

    public Optional<Ala> getAlaById(Long id) {
        return alaRepository.findById(id);
    }
    public Optional<Ala> findByNome(String nome) {
        return alaRepository.findByNome(nome);
    }
    public List<Ala> listarAlas() {
        return alaRepository.findAllByOrderByNome();
    }
    public List<Ala> getAllAlas() {
        return alaRepository.findAll();
    }

    public void deleteAla(Ala ala) {
        alaRepository.delete(ala);
    }

    public Ala updateAla(Ala alaDetails, Ala alaToUpdate) {
        BeanUtils.copyProperties(alaDetails, alaToUpdate, "idAla");
        return alaRepository.save(alaToUpdate);
    }
}

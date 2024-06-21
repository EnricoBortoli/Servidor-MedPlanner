package medplanner.controller;


import jakarta.validation.Valid;
import medplanner.model.Ala;
import medplanner.repository.AlaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("ala")
public class AlaController {

    @Autowired
    private AlaRepository alaRepository;

    @PostMapping("/salvar")
    public ResponseEntity<?> salvarAla(@PathVariable Long id, @Valid @RequestBody Ala alaDetails, BindingResult bindingResult) {
        if (alaDetails.getIdAla() == null) {

            if (bindingResult.hasErrors()) {
                List<String> errors = bindingResult.getAllErrors().stream()
                        .map(error -> error.getDefaultMessage())
                        .collect(Collectors.toList());
                return ResponseEntity.badRequest().body(errors);
            }

            Optional<Ala> existingAlaByNome = alaRepository.findByNome(alaDetails.getNome());
            if (existingAlaByNome.isPresent() && !existingAlaByNome.get().getIdAla().equals(alaDetails.getIdAla())) {
                return ResponseEntity.badRequest().body("Esse nome já existe");
            }

            Optional<Ala> existingAlaBySigla = alaRepository.findBySigla(alaDetails.getSigla());
            if (existingAlaBySigla.isPresent() && !existingAlaBySigla.get().getIdAla().equals(alaDetails.getIdAla())) {
                return ResponseEntity.badRequest().body("Essa sigla já existe");
            }

            Ala savedAla = alaRepository.save(alaDetails);
            return ResponseEntity.ok(savedAla);
        } else {
            Optional<Ala> ala = alaRepository.findById(id);

            if (ala.isPresent()) {
                Optional<Ala> existingAlaByNome = alaRepository.findByNome(alaDetails.getNome());
                if (existingAlaByNome.isPresent() && !existingAlaByNome.get().getIdAla().equals(alaDetails.getIdAla())) {
                    return ResponseEntity.badRequest().body("Esse nome já existe");
                }

                Optional<Ala> existingAlaBySigla = alaRepository.findBySigla(alaDetails.getSigla());
                if (existingAlaBySigla.isPresent() && !existingAlaBySigla.get().getIdAla().equals(alaDetails.getIdAla())) {
                    return ResponseEntity.badRequest().body("Essa sigla já existe");
                }

                Ala alaToUpdate = ala.get();
                BeanUtils.copyProperties(alaDetails, alaToUpdate);
                return ResponseEntity.ok(alaRepository.save(alaToUpdate));

            } else {
                return ResponseEntity.notFound().build();
            }
        }
    }

    @GetMapping("/listar")
    public List<Ala> listarAlas() {
        return alaRepository.findAll();
    }

    @GetMapping("/buscar")
    public ResponseEntity<Ala>buscarAlaById(@PathVariable Long id) {
        Optional<Ala> ala = alaRepository.findById(id);
        if (ala.isPresent()) {
            return ResponseEntity.ok(ala.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deleteAla(@PathVariable Long id) {
        Optional<Ala> ala = alaRepository.findById(id);
        if (ala.isPresent()) {
            alaRepository.delete(ala.get());
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

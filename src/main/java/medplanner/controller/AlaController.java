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

    @PostMapping("/criar")
    public ResponseEntity<?> criarAla(@RequestBody @Valid Ala ala, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }
        Ala savedAla = alaRepository.save(ala);
        return ResponseEntity.ok(savedAla);
    }

    @GetMapping("/listar")
    public List<Ala> listarAlas() {
        return alaRepository.findAll();
    }
    @GetMapping("/buscar")
    public ResponseEntity<Ala>buscarAlaById(@PathVariable Long idAla) {
        Optional<Ala> ala = alaRepository.findById(idAla);
        if (ala.isPresent()) {
            return ResponseEntity.ok(ala.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PutMapping("/salvar")
    public ResponseEntity<?> salvarAla(@PathVariable Long idAla, @Valid @RequestBody Ala alaDetails, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }

        Optional<Ala> ala = alaRepository.findById(idAla);
        if (ala.isPresent()) {
            Ala alaToUpdate = ala.get();
            alaToUpdate.setNome(alaDetails.getNome());
            alaToUpdate.setSigla(alaDetails.getSigla());
            alaToUpdate.setAndar(alaDetails.getAndar());
            Ala updatedAla = alaRepository.save(alaToUpdate);
            return ResponseEntity.ok(updatedAla);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deleteAla(@PathVariable Long idAla) {
        Optional<Ala> ala = alaRepository.findById(idAla);
        if (ala.isPresent()) {
            alaRepository.delete(ala.get());
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

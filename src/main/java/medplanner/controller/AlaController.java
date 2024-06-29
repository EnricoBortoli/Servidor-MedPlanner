package medplanner.controller;

import jakarta.validation.Valid;
import medplanner.model.Ala;
import medplanner.services.AlaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("ala")
public class AlaController {

    @Autowired
    private AlaService alaService;

    @PostMapping("/salvar")
    public ResponseEntity<?> salvarAla(@Valid @RequestBody Ala alaDetails, BindingResult bindingResult) {
        List<String> errors = alaService.validateAla(alaDetails, bindingResult);
        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(errors);
        }

        if (alaDetails.getIdAla() == null) {
            Ala savedAla = alaService.saveAla(alaDetails);
            return ResponseEntity.ok(savedAla);
        } else {
            Optional<Ala> ala = alaService.getAlaById(alaDetails.getIdAla());

            if (ala.isPresent()) {
                Ala alaToUpdate = ala.get();
                Ala updatedAla = alaService.updateAla(alaDetails, alaToUpdate);
                return ResponseEntity.ok(updatedAla);
            } else {
                return ResponseEntity.notFound().build();
            }
        }
    }

    @GetMapping("/listar")
    public List<Ala> listarAlas() {
        return alaService.getAllAlasOrderedByName();
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<Ala> buscarAlaById(@PathVariable Long id) {
        Optional<Ala> ala = alaService.getAlaById(id);
        if (ala.isPresent()) {
            return ResponseEntity.ok(ala.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/buscar/nome/{nome}")
    public ResponseEntity<Ala> buscarAlaByNome(@PathVariable String nome) {
        Optional<Ala> ala = alaService.findByNome(nome);
        if (ala.isPresent()) {
            return ResponseEntity.ok(ala.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deleteAla(@PathVariable Long id) {
        Optional<Ala> ala = alaService.getAlaById(id);
        if (ala.isPresent()) {
            alaService.deleteAla(ala.get());
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

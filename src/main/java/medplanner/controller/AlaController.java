package medplanner.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import medplanner.model.Ala;
import medplanner.services.AlaService;
import medplanner.services.SalaService;

@RestController
@RequestMapping("/ala")
public class AlaController {

    @Autowired
    private AlaService alaService;

    @Autowired
    private SalaService salaService;

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

                // Atualizar a situação das salas associadas
                salaService.atualizarSituacaoSalas(updatedAla.getIdAla(), updatedAla.getSituacao());

                return ResponseEntity.ok(updatedAla);
            } else {
                return ResponseEntity.notFound().build();
            }
        }
    }

    @GetMapping("/listar")
    public List<Ala> listarAlas() {
        return alaService.listarAlas();
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

    @GetMapping("/buscar/situacao/{situacao}")
    public ResponseEntity<List<Ala>> buscarAlaPorSituacao(@PathVariable String situacao) {
        List<Ala> alas = alaService.findBySituacao(situacao);
        return ResponseEntity.ok(alas);
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

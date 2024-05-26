package medplanner.controller;


import medplanner.exception.ResourceNotFoundException;
import medplanner.model.Ala;
import medplanner.services.AlaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("ala")
public class AlaController {

    @Autowired
    private AlaService alaService;

    @PostMapping
    public Ala createAla(@RequestBody Ala ala) {
        return alaService.createAla(ala);
    }

    @GetMapping
    public List<Ala> getAllAlas() {
        return alaService.getAllAlas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ala> getAlaById(@PathVariable Long id) {
        Ala ala = alaService.getAlaById(id).orElseThrow(() -> new ResourceNotFoundException("Ala não encontrada pelo id: " + id));
        return ResponseEntity.ok().body(ala);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ala> updateAla(@PathVariable Long id, @RequestBody Ala alaDetails) {
        Ala updatedAla = alaService.updateAla(id, alaDetails);
        return ResponseEntity.ok(updatedAla);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAla(@PathVariable Long id) {
        alaService.deleteAla(id);
        return ResponseEntity.noContent().build();
    }
}

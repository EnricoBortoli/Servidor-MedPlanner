package medplanner.controller;

import medplanner.model.Locacao;
import medplanner.services.LocacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/locacao")
public class LocacaoController {

    @Autowired
    private LocacaoService locacaoService;


    public ResponseEntity<?> salvarLocacao(@Valid @RequestBody Locacao locacaoDetails, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }

        try {
            Locacao locacao = locacaoService.salvarLocacao(locacaoDetails);
            return ResponseEntity.ok(locacao);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/salvar")
    public ResponseEntity<?> atualizarLocacao(@PathVariable Long id, @Valid @RequestBody Locacao locacaoDetails, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        try {
            Locacao locacao;
            if (locacaoDetails.getIdLocacao() == null) {
                locacao = locacaoService.salvarLocacao(locacaoDetails);
            } else {
               locacao = locacaoService.atualizarLocacao(id, locacaoDetails);
            }
            return ResponseEntity.ok(locacao);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/listar")
    public List<Locacao> listarLocacoes() {
        return locacaoService.listarLocacoes();
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<?> buscarLocacaoById(@PathVariable Long id) {
        Optional<Locacao> locacao = locacaoService.buscarLocacaoById(id);
        return locacao.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteLocacao(@PathVariable Long id) {
        try {
            locacaoService.deletarLocacao(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

package medplanner.controller;

import medplanner.dto.LocacaoDTO;
import medplanner.model.Locacao;
import medplanner.services.LocacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/locacao")
public class LocacaoController {

    @Autowired
    private LocacaoService locacaoService;

    @PostMapping("/salvar")
    public ResponseEntity<?> atualizarLocacao(@Valid @RequestBody LocacaoDTO locacaoDetails, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        try {
            Locacao locacao;
            if (locacaoDetails.getIdLocacao() == null) {
                locacao = locacaoService.verificarUsuarioAntesDeSalvar(locacaoDetails.getIdUsuario(), locacaoDetails);
            } else {
                locacao = locacaoService.atualizarLocacao(locacaoDetails.getIdUsuario(), locacaoDetails);
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

    @GetMapping("/buscar")
    public ResponseEntity buscarLocacao(@RequestParam Map<String, String> parametros) {
        if (parametros.isEmpty()) {
            return ResponseEntity.ok().body(locacaoService.listarLocacoes());
        }
        if (parametros.get("id") != null) {
            Optional<Locacao> locacao = locacaoService
                    .buscarLocacaoById(Long.parseLong(parametros.get("id")));
            return ResponseEntity.ok().body(locacao);
        }
        if (parametros.get("sala") != null) {
            List<Locacao> locacao = locacaoService
                    .findBySala(parametros.get("sala"));
            return ResponseEntity.ok().body(locacao);
        }
        if (parametros.get("medico") != null) {
            List<Locacao> locacao = locacaoService
                    .findByMedico(parametros.get("medico"));
            return ResponseEntity.ok().body(locacao);
        }
        return ResponseEntity.badRequest().body("Parâmetros de pesquisa inválidos");
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

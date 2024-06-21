package medplanner.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import medplanner.model.Profissional;

@RestController
@RequestMapping("profissional")
public class ProfissionalController {

    @Autowired
    private medplanner.services.ProfissionalService profissionalService;

    @GetMapping("/listar")
    public List<Profissional> listarProfissionais() {
        return profissionalService.listarProfissionais();
    }

    @GetMapping("/buscar")
    public ResponseEntity<?> buscarProfissionais(@RequestBody Map<String, String> parametros) {
        return profissionalService.buscarProfissionais(parametros);
    }

    @PostMapping("/salvar")
    public ResponseEntity<?> salvarProfissional(@RequestBody @Valid Profissional profissional, BindingResult result,
            @AuthenticationPrincipal UserDetails userDetails) {
        return profissionalService.salvarProfissional(profissional, result, userDetails);
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<String> deletarProfissional(@PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return profissionalService.deletarProfissional(id, userDetails);
    }
}

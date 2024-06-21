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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import medplanner.model.Especialidade;

@RestController
@RequestMapping("especialidade")
public class EspecialidadeController {

    @Autowired
    private medplanner.services.EspecialidadeService especialidadeService;

    @GetMapping("/listar")
    public List<Especialidade> listarEspecialidade() {
        return especialidadeService.listarEspecialidades();
    }

    @GetMapping("/buscar")
    public ResponseEntity<?> buscarEspecialidade(@RequestParam Map<String, String> parametros) {
        return especialidadeService.buscarEspecialidades(parametros);
    }

    @PostMapping("/salvar")
    public ResponseEntity<?> salvarEspecialidade(@RequestBody @Valid Especialidade especialidade,
            BindingResult result) {
        return especialidadeService.salvarEspecialidade(especialidade, result);
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<String> deletarEspecialidade(@PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return especialidadeService.deletarEspecialidade(id, userDetails);
    }
}

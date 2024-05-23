package medplanner.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import medplanner.exception.CustomExceptionHandler;
import medplanner.model.Especialidade;
import medplanner.repository.EspecialidadeRepository;

@RestController
@RequestMapping("especialidade")
public class EspecialidadeController {

    @Autowired
    private EspecialidadeRepository especialidadeRepository;

    @Autowired
    private CustomExceptionHandler customExceptionHandler;

    @GetMapping("/listar")
    public List<Especialidade> listarEspecialidade() {
        return especialidadeRepository.findAll();
    }

    @GetMapping("/buscar")
    public ResponseEntity buscarEspecialidade(@RequestParam Map<String, String> parametros) {
        if (parametros.isEmpty()) {
            return ResponseEntity.ok().body(especialidadeRepository.findAll());
        }
        if (parametros.get("id") != null) {
            return ResponseEntity.ok()
                    .body(especialidadeRepository.findAllByIdEspecialidade(parametros.get("id")));
        }
        if (parametros.get("nome") != null) {
            return ResponseEntity.ok().body(especialidadeRepository.findByNomeStartingWith(parametros.get("nome")));
        }
        if (parametros.get("sigla") != null) {
            return ResponseEntity.ok().body(especialidadeRepository.findBySiglaStartingWith(parametros.get("sigla")));
        }
        return ResponseEntity.badRequest().body("Parâmetros de pesquisa inválidos");
    }

    @PostMapping("/salvar")
    public ResponseEntity<?> salvarEspecialidade(@RequestBody @Valid Especialidade especialidade,
            BindingResult result) {
        List<String> errors = new ArrayList<>();

        if (result.hasErrors()) {
            errors.addAll(
                    result.getFieldErrors().stream().map(FieldError::getDefaultMessage).collect(Collectors.toList()));
        }

        if (especialidadeRepository.findByNomeStartingWith(especialidade.getNome()).size() > 0) {
            errors.add("Nome já existe.");
        }

        if (especialidadeRepository.findBySiglaStartingWith(especialidade.getSigla()).size() > 0) {
            errors.add("Sigla já existe.");
        }

        if (!errors.isEmpty()) {
            Map<String, List<String>> errorResponse = new HashMap<>();
            errorResponse.put("errors", errors);
            return ResponseEntity.badRequest().body(errorResponse);
        }

        try {
            especialidadeRepository.save(especialidade);
            return ResponseEntity.ok().build();
        } catch (DataIntegrityViolationException e) {
            return customExceptionHandler.handleDataIntegrityExceptions(e);
        }
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<String> deletarEspecialidade(@PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        if (!userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMINISTRADOR"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Apenas usuários com cargo de ADMINISTRADOR podem excluir registros.");
        }

        Especialidade especialidade = especialidadeRepository.findById(id).orElse(null);

        if (especialidade != null) {
            especialidadeRepository.delete(especialidade);
            return ResponseEntity.ok("Especialidade deletada com sucesso");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

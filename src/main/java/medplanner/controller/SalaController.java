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
import medplanner.model.Recurso;
import medplanner.model.Sala;
import medplanner.repository.SalaRepository;

@RestController
@RequestMapping("sala")
public class SalaController {

    @Autowired
    private SalaRepository salaRepository;

    @Autowired
    private CustomExceptionHandler customExceptionHandler;

    @GetMapping("/buscar")
    public ResponseEntity buscarUsuario(@RequestParam Map<String, String> parametros) {
        if (parametros.isEmpty()) {
            return ResponseEntity.ok().body(salaRepository.findAll());
        }
        if (parametros.containsKey("idSala")) {
            try {
                Long idSala = Long.parseLong(parametros.get("idSala"));
                return ResponseEntity.ok().body(salaRepository.findAllByIdSala(idSala));
            } catch (NumberFormatException e) {
                return ResponseEntity.badRequest().body("O parâmetro idSala deve ser um número.");
            }
        }
        if (parametros.containsKey("nomeSala")) {
            String nomeSala = parametros.get("nomeSala");
            String nomeSalaComCuringa = "%" + nomeSala + "%";
            return ResponseEntity.ok().body(salaRepository.findByNomeSala(nomeSalaComCuringa));
        }
        if (parametros.containsKey("situacao")) {
            return ResponseEntity.ok().body(salaRepository.findAllBySituacao(parametros.get("situacao")));
        }
        return ResponseEntity.badRequest().body("Parâmetro de pesquisa inválidos");
    }

    @PostMapping("/salvar")
    public ResponseEntity<?> salvarSala(@RequestBody @Valid Sala sala, BindingResult result,
            @AuthenticationPrincipal UserDetails userDetails) {

        boolean hasRequiredAuthority = userDetails.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ADMINISTRADOR") ||
                        authority.getAuthority().equals("RECEPCAO"));

        if (!hasRequiredAuthority) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Apenas usuários com cargo de ADMINISTRADOR ou RECEPÇÃO podem cadastrar novas salas.");
        }

        List<String> errors = new ArrayList<>();

        // if (salaRepository.findByNomeSala(sala.getNomeSala()).isEmpty() ) {
        // errors.add("Já existe uma sala com esse nome.");
        // }

        if (result.hasErrors()) {
            errors.addAll(result.getFieldErrors().stream().map(FieldError::getDefaultMessage)
                    .collect(Collectors.toList()));
        }

        if (!errors.isEmpty()) {
            Map<String, List<String>> errorResponse = new HashMap<>();
            errorResponse.put("errors", errors);
            return ResponseEntity.badRequest().body(errorResponse);
        }

        try {
            if (sala.getRecursos() != null) {
                for (Recurso recurso : sala.getRecursos()) {
                    recurso.setSala(sala);
                }
            }
            salaRepository.save(sala);
            return ResponseEntity.ok().build();
        } catch (DataIntegrityViolationException e) {
            return customExceptionHandler.handleDataIntegrityExceptions(e);
        }
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<String> deletarSala(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        boolean hasRequiredAuthority = userDetails.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ADMINISTRADOR") ||
                        authority.getAuthority().equals("RECEPCAO"));

        if (!hasRequiredAuthority) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Apenas usuários com cargo de ADMINISTRADOR ou RECEPÇÃO podem excluir registros.");
        }

        Sala sala = salaRepository.findById(id).orElse(null);

        if (sala != null) {
            salaRepository.delete(sala);
            return ResponseEntity.ok("Sala deletada com sucesso!");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}

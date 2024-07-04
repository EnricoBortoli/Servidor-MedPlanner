package medplanner.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import medplanner.model.Ala;
import medplanner.model.Sala;
import medplanner.services.AlaService;
import medplanner.services.SalaService;

@RestController
@RequestMapping("/sala")
public class SalaController {

    @Autowired
    private SalaService salaService;

    @Autowired
    private AlaService alaService;

    @Autowired
    private CustomExceptionHandler customExceptionHandler;

    @GetMapping("/buscar")
    public ResponseEntity<?> buscarSalas(@RequestParam Map<String, String> parametros) {
        if (parametros.isEmpty()) {
            return ResponseEntity.ok(salaService.buscarTodasSalas());
        }
        if (parametros.containsKey("idSala")) {
            try {
                Long idSala = Long.parseLong(parametros.get("idSala"));
                return salaService.buscarSalaPorId(idSala)
                        .map(ResponseEntity::ok)
                        .orElse(ResponseEntity.notFound().build());
            } catch (NumberFormatException e) {
                return ResponseEntity.badRequest().body("O parâmetro idSala deve ser um número.");
            }
        }
        if (parametros.containsKey("nomeSala")) {
            String nomeSala = parametros.get("nomeSala");
            return ResponseEntity.ok(salaService.buscarSalasPorNome(nomeSala));
        }
        if (parametros.containsKey("situacao") && parametros.containsKey("idAla")) {
            try {
                Long idAla = Long.parseLong(parametros.get("idAla"));
                String situacao = parametros.get("situacao");
                return ResponseEntity.ok(salaService.buscarSalasPorAlaESituacao(idAla, situacao));
            } catch (NumberFormatException e) {
                return ResponseEntity.badRequest().body("Os parâmetros idAla e situacao devem ser válidos.");
            }
        }
        if (parametros.containsKey("situacao")) {
            String situacao = parametros.get("situacao");
            return ResponseEntity.ok(salaService.buscarSalasPorSituacao(situacao));
        }
        if (parametros.containsKey("idAla")) {
            try {
                Long idAla = Long.parseLong(parametros.get("idAla"));
                return ResponseEntity.ok(salaService.buscarSalasPorAla(idAla));
            } catch (NumberFormatException e) {
                return ResponseEntity.badRequest().body("O parâmetro idAla deve ser um número.");
            }
        }
        return ResponseEntity.badRequest().body("Parâmetros de pesquisa inválidos.");
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

        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.toList());
            Map<String, List<String>> errorResponse = new HashMap<>();
            errorResponse.put("errors", errors);
            return ResponseEntity.badRequest().body(errorResponse);
        }

        Ala ala = alaService.getAlaById(sala.getAla().getIdAla()).orElse(null);
        if (ala == null || !"A".equals(ala.getSituacao())) {
            return ResponseEntity.badRequest().body("A Ala selecionada está inativa!");
        }

        try {
            Sala savedSala = salaService.salvarSala(sala);
            return ResponseEntity.ok(savedSala);
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

        try {
            salaService.deletarSala(id);
            return ResponseEntity.ok("Sala deletada com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao deletar a sala.");
        }
    }
}

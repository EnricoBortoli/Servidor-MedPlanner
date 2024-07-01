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
import medplanner.dto.RecursoDTO;
import medplanner.exception.CustomExceptionHandler;
import medplanner.model.Recurso;

import medplanner.services.RecursoService;

@RestController
@RequestMapping("/recurso")
public class RecursoController {

    @Autowired
    private RecursoService recursoService;

    @Autowired
    private CustomExceptionHandler customExceptionHandler;

    @GetMapping("/buscar")
    public ResponseEntity<?> buscarRecursos(@RequestParam Map<String, String> parametros) {
        List<Recurso> recursos;

        if (parametros.isEmpty()) {
            recursos = recursoService.buscarTodosRecursos();
        } else if (parametros.containsKey("idRecurso")) {
            try {
                Long idRecurso = Long.parseLong(parametros.get("idRecurso"));
                return recursoService.buscarRecursoPorId(idRecurso)
                        .map(ResponseEntity::ok)
                        .orElse(ResponseEntity.notFound().build());
            } catch (NumberFormatException e) {
                return ResponseEntity.badRequest().body("O parâmetro idRecurso deve ser um número.");
            }
        } else if (parametros.containsKey("nomeRecurso")) {
            String nomeRecurso = parametros.get("nomeRecurso");
            recursos = recursoService.buscarRecursosPorNome(nomeRecurso);
        } else if (parametros.containsKey("idSala")) {
            try {
                Long idSala = Long.parseLong(parametros.get("idSala"));
                recursos = recursoService.buscarRecursosPorIdSala(idSala);
            } catch (NumberFormatException e) {
                return ResponseEntity.badRequest().body("O parâmetro idSala deve ser um número.");
            }
        } else {
            return ResponseEntity.badRequest().body("Parâmetros de pesquisa inválidos");
        }

        List<RecursoDTO> recursoDTOs = recursos.stream().map(recurso -> new RecursoDTO(
                recurso.getIdRecurso(),
                recurso.getNomeRecurso(),
                recurso.getDescricao(),
                recurso.getSala() != null ? recurso.getSala().getIdSala() : null)).collect(Collectors.toList());

        return ResponseEntity.ok(recursoDTOs);
    }

    @PostMapping("/salvar")
    public ResponseEntity<?> salvarRecurso(@RequestBody @Valid RecursoDTO recursoDTO, BindingResult result,
            @AuthenticationPrincipal UserDetails userDetails) {

        boolean hasRequiredAuthority = userDetails.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ADMINISTRADOR") ||
                        authority.getAuthority().equals("RECEPCAO"));

        if (!hasRequiredAuthority) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Apenas usuários com cargo de ADMINISTRADOR ou RECEPÇÃO podem cadastrar/editar novos recursos.");
        }

        List<String> errors = new ArrayList<>();

        if (result.hasErrors()) {
            errors.addAll(result.getFieldErrors().stream().map(FieldError::getDefaultMessage)
                    .collect(Collectors.toList()));
        }

        Long idSala = recursoDTO.getIdSala();
        if (idSala == null) {
            errors.add("O recurso deve incluir um idSala válido.");
        }

        if (!errors.isEmpty()) {
            Map<String, List<String>> errorResponse = new HashMap<>();
            errorResponse.put("errors", errors);
            return ResponseEntity.badRequest().body(errorResponse);
        }

        try {
            Recurso recurso = new Recurso();
            recurso.setNomeRecurso(recursoDTO.getNomeRecurso());
            recurso.setDescricao(recursoDTO.getDescricao());

            Recurso savedRecurso = recursoService.salvarRecursoComSala(recurso, idSala);
            return ResponseEntity.ok(savedRecurso);
        } catch (DataIntegrityViolationException e) {
            return customExceptionHandler.handleDataIntegrityExceptions(e);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ocorreu um erro ao salvar o recurso. Por favor, tente novamente.");
        }
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<String> deletarRecurso(@PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        boolean hasRequiredAuthority = userDetails.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ADMINISTRADOR") ||
                        authority.getAuthority().equals("RECEPCAO"));

        if (!hasRequiredAuthority) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Apenas usuários com cargo de ADMINISTRADOR ou RECEPÇÃO podem excluir registros.");
        }

        try {
            recursoService.deletarRecurso(id);
            return ResponseEntity.ok("Recurso deletado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao deletar o recurso.");
        }
    }
}

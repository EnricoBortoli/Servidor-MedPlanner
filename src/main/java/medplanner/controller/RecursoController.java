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
import medplanner.repository.RecursoRepository;

@RestController
@RequestMapping("recurso")
public class RecursoController {

     @Autowired
    private RecursoRepository recursoRepository;

    @Autowired
    private CustomExceptionHandler customExceptionHandler;
  
    @GetMapping("/buscar")
    public ResponseEntity buscarRecurso(@RequestParam Map<String, String> parametros) {
        if (parametros.isEmpty()) {
            return ResponseEntity.ok().body(recursoRepository.findAll());
        }
        if (parametros.get("idSala") != null) {
            return ResponseEntity.ok().body(recursoRepository.findAllByIdRecurso(parametros.get("idRecurso")));
        }
        if (parametros.get("nomeRecurso") != null) {
            return ResponseEntity.ok().body(recursoRepository.findByNomeRecurso((parametros.get("nomeRecurso"))));
        }
        return ResponseEntity.badRequest().body("Parâmetro de pesquisa inválidos");
    }

     @PostMapping("/salvar")
    public ResponseEntity<?> salvarRecurso(@RequestBody @Valid Recurso recurso, BindingResult result,
            @AuthenticationPrincipal UserDetails userDetails) {
        if (!userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ADMINISTRADOR"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Apenas usuários com cargo de ADMINISTRADOR ou RECEPCIONISTA podem salvar profissionais.");
        }

        List<String> errors = new ArrayList<>();

    //    if (recursoRepository.findByNomeRecurso(recurso.getNomeRecurso()) != null) {
     //       errors.add("Já existe um recurso com esse nome.");
     //   }

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
            recursoRepository.save(recurso);
            return ResponseEntity.ok().build();
        } catch (DataIntegrityViolationException e) {
            return customExceptionHandler.handleDataIntegrityExceptions(e);
        }
    }
    
    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<String> deletarRecurso(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        if (!userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMINISTRADOR"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Apenas usuários com cargo de ADMINISTRADOR podem excluir registros.");
        }

        Recurso recurso = recursoRepository.findById(id).orElse(null);

        if (recurso != null) {
            recursoRepository.delete(recurso);
            return ResponseEntity.ok("Recurso deletado com sucesso");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
}

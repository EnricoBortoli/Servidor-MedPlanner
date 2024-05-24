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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import medplanner.exception.CustomExceptionHandler;
import medplanner.model.Profissional;
import medplanner.repository.ProfissionalRepository;
import medplanner.services.EmailService;
import medplanner.services.TokenService;

@RestController
@RequestMapping("profissional")
public class ProfissionalController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private ProfissionalRepository profissionalRepository;

    @Autowired
    private CustomExceptionHandler customExceptionHandler;

    @Autowired
    private EmailService emailService;

    @GetMapping("/listar")
    public List<Profissional> listarProfissionais() {
        return profissionalRepository.findAll();
    }

    @GetMapping("/buscar")
    public ResponseEntity<?> buscarProfissionais(@RequestBody Map<String, String> parametros) {
        if (parametros.isEmpty()) {
            return ResponseEntity.ok().body(profissionalRepository.findAll());
        }
        if (parametros.containsKey("id")) {
            List<Profissional> profissionais = profissionalRepository.findAllByIdProfissional(parametros.get("id"));
            return ResponseEntity.ok().body(profissionais);
        }
        if (parametros.containsKey("numCrm")) {
            List<Profissional> profissionais = profissionalRepository
                    .findByNumCrmStartingWith(parametros.get("numCrm"));
            return ResponseEntity.ok().body(profissionais);
        }
        if (parametros.containsKey("ufCrm")) {
            List<Profissional> profissionais = profissionalRepository.findByUfCrmStartingWith(parametros.get("ufCrm"));
            return ResponseEntity.ok().body(profissionais);
        }
        return ResponseEntity.badRequest().body("Parâmetros de pesquisa inválidos");
    }

    @PostMapping("/salvar")
    public ResponseEntity<?> salvarProfissional(@RequestBody @Valid Profissional profissional, BindingResult result,
            @AuthenticationPrincipal UserDetails userDetails) {
        if (!userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMINISTRADOR"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Apenas usuários com cargo de ADMINISTRADOR podem salvar profissionais.");
        }

        List<String> errors = new ArrayList<>();

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
            String passwordCriptografada = new BCryptPasswordEncoder().encode(profissional.getPassword());
            profissional.setPassword(passwordCriptografada);
            profissionalRepository.save(profissional);
            return ResponseEntity.ok().build();
        } catch (DataIntegrityViolationException e) {
            return customExceptionHandler.handleDataIntegrityExceptions(e);
        }
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<String> deletarProfissional(@PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        if (!userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMINISTRADOR"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Apenas usuários com cargo de ADMINISTRADOR podem excluir profissionais.");
        }

        Profissional profissional = profissionalRepository.findById(id).orElse(null);

        if (profissional != null) {
            profissionalRepository.delete(profissional);
            return ResponseEntity.ok("Profissional deletado com sucesso");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}

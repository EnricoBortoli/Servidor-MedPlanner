package medplanner.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import medplanner.exception.CustomExceptionHandler;
import medplanner.model.Usuario;
import medplanner.repository.UsuarioRepository;
import medplanner.services.TokenService;
import medplanner.validation.CPFValidator;

@RestController
@RequestMapping("usuario")
public class UsuarioController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CustomExceptionHandler customExceptionHandler;

    @Autowired
    private CPFValidator cpfValidator;

    @Autowired
    private LocalValidatorFactoryBean validator;

    @RequestMapping("/listar")
    public List<Usuario> listarUsuario() {
        return usuarioRepository.findAll();
    }

    @GetMapping("/buscar")
    public ResponseEntity buscarUsuario(@RequestParam Map<String, String> parametros) {
        if (parametros.isEmpty()) {
            return ResponseEntity.ok().body(usuarioRepository.findAll());
        }
        if (parametros.get("id") != null) {
            return ResponseEntity.ok().body(usuarioRepository.findAllByIdUsuario(parametros.get("id")));
        }
        if (parametros.get("nome") != null) {
            return ResponseEntity.ok().body(usuarioRepository.findAllByNomeUsuario((parametros.get("nome"))));
        }
        return ResponseEntity.badRequest().body("Parâmetro de pesquisa inválidos");
    }

    @PostMapping("/salvar")
    public ResponseEntity<?> salvarUsuario(@RequestBody @Valid Usuario usuario, BindingResult result) {
    List<String> errors = new ArrayList<>();

        if (result.hasErrors()) {
        errors.addAll(result.getFieldErrors().stream().map(FieldError::getDefaultMessage).collect(Collectors.toList()));
    }
        
        if (!cpfValidator.isValid(usuario.getCpf(), null)) {
            errors.add("CPF inválido");
        }
    
        if (usuarioRepository.findByUsername(usuario.getUsername()) != null) {
            errors.add("Usuário já existe.");
        }
    
        if (usuarioRepository.findByCPF(usuario.getCpf()) != null) {
            errors.add("CPF já existe.");
        }
    
        if (!errors.isEmpty()) {
            Map<String, List<String>> errorResponse = new HashMap<>();
            errorResponse.put("errors", errors);
            return ResponseEntity.badRequest().body(errorResponse);
        }
    
        try {
            String passwordCriptografada = new BCryptPasswordEncoder().encode(usuario.getPassword());
            usuario.setPassword(passwordCriptografada);
            usuarioRepository.save(usuario);
            return ResponseEntity.ok().build();
        } catch (DataIntegrityViolationException e) {
            return customExceptionHandler.handleDataIntegrityExceptions(e);
        }
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<String> deletarUsuario(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
    if (!userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMINISTRADOR"))) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Apenas usuários com cargo de ADMINISTRADOR podem excluir registros.");
    }

    Usuario usuario = usuarioRepository.findById(id).orElse(null);

    if (usuario != null) {
        usuarioRepository.delete(usuario);
        return ResponseEntity.ok("Usuário deletado com sucesso");
    } else {
        return ResponseEntity.notFound().build();
    }
}

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody Usuario usuario) {
    if (StringUtils.isEmpty(usuario.getUsername()) || StringUtils.isEmpty(usuario.getPassword())) {
        return ResponseEntity.badRequest().body("Nome de usuário e senha são obrigatórios.");
    }

    try {
        var usernamePassword = new UsernamePasswordAuthenticationToken(usuario.getUsername(),
                usuario.getPassword());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        Usuario usuarioAutenticado = (Usuario) auth.getPrincipal();

        var token = tokenService.generateToken(usuarioAutenticado);

        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("usuario", usuarioAutenticado.getNome());

        return ResponseEntity.ok(response);
    } catch (Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
}

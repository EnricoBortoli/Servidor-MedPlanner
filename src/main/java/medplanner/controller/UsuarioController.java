package medplanner.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    public ResponseEntity<?> salvarUsuario(@RequestBody @Valid Usuario usuario) {
        // Validar CPF
        if (!cpfValidator.isValid(usuario.getCpf(), null)) {
            return customExceptionHandler.handleCPFInvalido();
        }

        try {
            if (usuarioRepository.findByUsername(usuario.getUsername()) != null) {
                throw new DataIntegrityViolationException("Usuário já existe.");
            }

            String passwordCriptografada = new BCryptPasswordEncoder().encode(usuario.getPassword());
            usuario.setPassword(passwordCriptografada);
            usuarioRepository.save(usuario);
            return ResponseEntity.ok().build();
        } catch (DataIntegrityViolationException e) {
            return customExceptionHandler.handleDataIntegrityExceptions(e);
        }
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<String> deletarUsuario(@PathVariable Long id) {
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

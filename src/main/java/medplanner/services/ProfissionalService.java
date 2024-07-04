package medplanner.services;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import medplanner.exception.CustomExceptionHandler;
import medplanner.model.Profissional;
import medplanner.model.Usuario;
import medplanner.repository.ProfissionalRepository;
import medplanner.repository.UsuarioRepository;
import medplanner.validation.CPFValidator;

@Service
public class ProfissionalService {

    @Autowired
    private ProfissionalRepository profissionalRepository;

    @Autowired
    private CustomExceptionHandler customExceptionHandler;

    @Autowired
    private CPFValidator cpfValidator;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EmailService emailService;

    public List<Profissional> listarProfissionais(UserDetails userDetails) {
        if (userDetails.getAuthorities().contains(new SimpleGrantedAuthority("MEDICO"))) {
            Optional<Usuario> optionalUsuario = usuarioRepository.buscarPorEmail(userDetails.getUsername());

            if (optionalUsuario.isPresent()) {
                Usuario usuario = optionalUsuario.get();
                Profissional profissional = (Profissional) usuario;
                List<Profissional> usuarioList = Collections.singletonList(profissional);
                return usuarioList;
            } else {
                return Collections.emptyList();
            }
        }
        List<Profissional> allUsuarios = profissionalRepository.findAll();
        List<Profissional> usuariosAtivos = new ArrayList<>(); // Initialize the list properly
        for (Profissional u : allUsuarios) {
            if (!"I".equals(u.getSituacao())) { // Use .equals to compare strings
                usuariosAtivos.add(u);
            }
        }

        return usuariosAtivos;
        // return profissionalRepository.findAll();
    }

    public ResponseEntity<?> buscarProfissionais(Map<String, String> parametros) {
        if (parametros.isEmpty()) {
            return ResponseEntity.ok().body(profissionalRepository.findAll());
        }
        if (parametros.containsKey("id")) {
            Optional<Profissional> profissional = profissionalRepository.findById(Long.parseLong(parametros.get("id")));
            return ResponseEntity.ok().body(profissional);
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

    public ResponseEntity<?> salvarProfissional(Profissional profissional, BindingResult result,
            UserDetails userDetails) {
        Usuario usuario = profissional;

        if (!userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ADMINISTRADOR"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Apenas usuários com cargo de ADMINISTRADOR podem salvar profissionais.");
        }
        List<String> errors = new ArrayList<>();

        if (!cpfValidator.isValid(usuario.getCpf(), null)) {
            errors.add("CPF inválido");
        }

        if (usuario.getIdUsuario() == null) {
            if (usuarioRepository.findByUsername(usuario.getUsername()) != null) {
                errors.add("Usuário já existe.");
            }

            if (profissionalRepository.findByCRM(profissional.getNumCrm()) != null) {
                errors.add("CRM já existe.");
            }

            // if (usuarioRepository.findByCPF(usuario.getCpf()) != null) {
            // errors.add("CPF já existe.");
            // }
        }

        if (result.hasErrors()) {
            errors.addAll(result.getFieldErrors().stream().map(FieldError::getDefaultMessage)
                    .collect(Collectors.toList()));
        }

        if (!errors.isEmpty()) {
            Map<String, List<String>> errorResponse = new HashMap<>();
            errorResponse.put("errors", errors);
            return ResponseEntity.badRequest().body(errorResponse);
        }

        if (usuario.getSituacao().equals("E")/* && usuario.getPassword() == null */) {
            usuario.setPassword(gerarSenha());
            sendPasswordEmail(usuario.getUsername(), usuario.getNome(), usuario.getPassword());
            // TODO fazer uma validação para usuario 'Em Validação' mas que venha com senha.
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

    public ResponseEntity<String> deletarProfissional(Long id, UserDetails userDetails) {
        if (!userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ADMINISTRADOR"))) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Apenas usuários com cargo de ADMINISTRADOR podem excluir profissionais.");
        }

        Profissional profissional = profissionalRepository.findById(id).orElse(null);

        if (profissional != null) {
            // o profissional nao pode ser deletato, apenas desativado e seus dados
            // sensiveis devem ser apagados
            profissional.setSituacao("I");
            profissional.setUsername("");
            profissional.setCpf("");
            profissionalRepository.save(profissional);

            // profissionalRepository.delete(profissional);
            return ResponseEntity.ok("Profissional desativado com sucesso");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private static final String CARACTERES = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+[]{};:?";

    public static String gerarSenha() {
        SecureRandom random = new SecureRandom();
        StringBuilder senha = new StringBuilder(8);

        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(CARACTERES.length());
            senha.append(CARACTERES.charAt(index));
        }
        return senha.toString();
    }

    private void sendPasswordEmail(String email, String name, String password) {
        String subject = "Sua nova senha";
        String body = "Olá " + name + ",\n\nSua nova senha é: " + password;
        emailService.sendEmail(email, subject, body);
    }

}

package medplanner.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import jakarta.validation.Valid;
import medplanner.exception.CustomExceptionHandler;
import medplanner.model.Especialidade;
import medplanner.repository.EspecialidadeRepository;

@Service
public class EspecialidadeService {

    @Autowired
    private EspecialidadeRepository especialidadeRepository;

    @Autowired
    private CustomExceptionHandler customExceptionHandler;

    public List<Especialidade> listarEspecialidades() {
        return especialidadeRepository.findAllByOrderByNome();
    }

    public ResponseEntity<?> buscarEspecialidades(Map<String, String> parametros) {
        if (parametros.isEmpty()) {
            return ResponseEntity.ok().body(especialidadeRepository.findAllByOrderByNome());
        }
        if (parametros.containsKey("id")) {
            return ResponseEntity.ok().body(especialidadeRepository.findAllByIdEspecialidade(parametros.get("id")));
        }
        if (parametros.containsKey("nome")) {
            return ResponseEntity.ok().body(especialidadeRepository.findByNomeStartingWith(parametros.get("nome")));
        }
        if (parametros.containsKey("sigla")) {
            return ResponseEntity.ok().body(especialidadeRepository.findBySiglaStartingWith(parametros.get("sigla")));
        }
        return ResponseEntity.badRequest().body("Parâmetros de pesquisa inválidos");
    }

    public ResponseEntity<?> salvarEspecialidade(@Valid Especialidade especialidade, BindingResult result) {
        List<String> errors = new ArrayList<>();

        if (result.hasErrors()) {
            errors.addAll(result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.toList()));
        }

        if (especialidade.getIdEspecialidade() == null) {
            if (!especialidadeRepository.findByNomeStartingWith(especialidade.getNome()).isEmpty()) {
                errors.add("Nome já existe.");
            }

            if (!especialidadeRepository.findBySiglaStartingWith(especialidade.getSigla()).isEmpty()) {
                errors.add("Sigla já existe.");
            }
        } else {
            List<Especialidade> especialidadesByName = especialidadeRepository
                    .findByNomeStartingWith(especialidade.getNome());
            if (especialidadesByName.size() > 1) {
                errors.add("Nome já existe.");
            } else if (especialidadesByName.size() == 1) {
                if (!especialidadesByName.get(0).getIdEspecialidade().equals(especialidade.getIdEspecialidade())) {
                    errors.add("Nome já existe.");
                }
            }
            List<Especialidade> especialidadesBySigla = especialidadeRepository
                    .findBySiglaStartingWith(especialidade.getSigla());
            if (especialidadesBySigla.size() > 1) {
                errors.add("Sigla já existe.");
            } else if (especialidadesBySigla.size() == 1) {
                if (!especialidadesBySigla.get(0).getIdEspecialidade().equals(especialidade.getIdEspecialidade())) {
                    errors.add("Sigla já existe.");
                }
            }
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

    public ResponseEntity<String> deletarEspecialidade(Long id, UserDetails userDetails) {
        if (!userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ADMINISTRADOR"))) {
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
package medplanner.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import medplanner.exception.CustomExceptionHandler;
import medplanner.model.Especialidade;
import medplanner.repository.EspecialidadeRepository;
import medplanner.services.EspecialidadeService;

class EspecialidadeServiceTests {

    @Mock
    private EspecialidadeRepository especialidadeRepository;

    @Mock
    private CustomExceptionHandler customExceptionHandler;

    @InjectMocks
    private EspecialidadeService especialidadeService;

    @Mock
    private BindingResult bindingResult;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListarEspecialidades() {
        Especialidade especialidade = new Especialidade();
        especialidade.setIdEspecialidade(1L);
        especialidade.setNome("Cardiologia");
        especialidade.setSigla("CA");

        when(especialidadeRepository.findAll()).thenReturn(Collections.singletonList(especialidade));

        List<Especialidade> especialidades = especialidadeService.listarEspecialidades();
        assertEquals(1, especialidades.size());
        assertEquals("Cardiologia", especialidades.get(0).getNome());
    }

    @Test
    void testBuscarEspecialidadesComParametro() {
        Especialidade especialidade = new Especialidade();
        especialidade.setIdEspecialidade(1L);
        especialidade.setNome("Cardiologia");
        especialidade.setSigla("CA");

        when(especialidadeRepository.findByNomeStartingWith("Cardio"))
                .thenReturn(Collections.singletonList(especialidade));

        Map<String, String> parametros = new HashMap<>();
        parametros.put("nome", "Cardio");

        ResponseEntity<?> response = especialidadeService.buscarEspecialidades(parametros);
        List<Especialidade> especialidades = (List<Especialidade>) response.getBody();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, especialidades.size());
        assertEquals("Cardiologia", especialidades.get(0).getNome());
    }

    @Test
    void testSalvarEspecialidadeComErro() {
        Especialidade especialidade = new Especialidade();
        especialidade.setNome("Cardiologia");
        especialidade.setSigla("CA");

        FieldError fieldError = new FieldError("especialidade", "nome", "Nome é obrigatório");
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldErrors()).thenReturn(Collections.singletonList(fieldError));

        ResponseEntity<?> response = especialidadeService.salvarEspecialidade(especialidade, bindingResult);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, List<String>> errorResponse = (Map<String, List<String>>) response.getBody();
        assertTrue(errorResponse.containsKey("errors"));
        assertEquals("Nome é obrigatório", errorResponse.get("errors").get(0));
    }

    @Test
    void testSalvarEspecialidadeComSucesso() {
        Especialidade especialidade = new Especialidade();
        especialidade.setNome("Cardiologia");
        especialidade.setSigla("CA");

        when(bindingResult.hasErrors()).thenReturn(false);
        when(especialidadeRepository.save(any(Especialidade.class))).thenReturn(especialidade);

        ResponseEntity<?> response = especialidadeService.salvarEspecialidade(especialidade, bindingResult);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    // @Test
    // void testDeletarEspecialidadeComSucesso() {
    // Especialidade especialidade = new Especialidade();
    // especialidade.setIdEspecialidade(1L);
    // especialidade.setNome("Cardiologia");

    // // Simulando um usuário com a autoridade de ADMINISTRADOR
    // UserDetails adminUser = User.withUsername("admin")
    // .password("password")
    // .authorities(new SimpleGrantedAuthority("ADMINISTRADOR"))
    // .build();

    // when(especialidadeRepository.findById(1L)).thenReturn(Optional.of(especialidade));

    // ResponseEntity<String> response =
    // especialidadeService.deletarEspecialidade(1L, adminUser);

    // assertEquals(HttpStatus.OK, response.getStatusCode());
    // assertEquals("Especialidade deletada com sucesso", response.getBody());
    // }

    @Test
    void testDeletarEspecialidadeSemPermissao() {
        UserDetails nonAdminUser = User.withUsername("user")
                .password("password")
                .authorities(new SimpleGrantedAuthority("USER"))
                .build();

        ResponseEntity<String> response = especialidadeService.deletarEspecialidade(1L, nonAdminUser);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Apenas usuários com cargo de ADMINISTRADOR podem excluir registros.", response.getBody());
    }
}
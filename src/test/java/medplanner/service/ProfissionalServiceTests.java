package medplanner.service;

import medplanner.exception.CustomExceptionHandler;
import medplanner.model.Profissional;
import medplanner.repository.ProfissionalRepository;
import medplanner.repository.UsuarioRepository;
import medplanner.services.ProfissionalService;
import medplanner.validation.CPFValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProfissionalServiceTests {

    @Mock
    private ProfissionalRepository profissionalRepository;

    @Mock
    private CustomExceptionHandler customExceptionHandler;

    @Mock
    private CPFValidator cpfValidator;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private ProfissionalService profissionalService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // @Test
    // void testSalvarProfissionalComErro() {
    // Profissional profissional = new Profissional();
    // profissional.setNome("Dr. João");
    // profissional.setUsername("joao@example.com");
    // profissional.setCpf("12345678909");
    // profissional.setUsername("MEDICO");
    // profissional.setUfCrm("SP");
    // profissional.setNumCrm("123456");

    // when(bindingResult.hasErrors()).thenReturn(false);
    // when(cpfValidator.isValid(any(), any())).thenReturn(true);
    // when(profissionalRepository.save(any(Profissional.class))).thenReturn(profissional);

    // UserDetails adminUser = User.withUsername("admin")
    // .password("password")
    // .authorities(new SimpleGrantedAuthority("ADMINISTRADOR"))
    // .build();

    // ResponseEntity<?> response =
    // profissionalService.salvarProfissional(profissional, bindingResult,
    // adminUser);

    // assertEquals(HttpStatus.OK, response.getStatusCode());
    // }

    @Test
    void testSalvarProfissionalSemPermissao() {
        Profissional profissional = new Profissional();
        profissional.setNome("Dr. João");
        profissional.setUsername("joao@example.com");
        profissional.setCpf("12345678909");
        profissional.setUsername("MEDICO");
        profissional.setUfCrm("SP");
        profissional.setNumCrm("123456");

        UserDetails nonAdminUser = User.withUsername("user")
                .password("password")
                .authorities(new SimpleGrantedAuthority("RECEPCAO"))
                .build();

        ResponseEntity<?> response = profissionalService.salvarProfissional(profissional, bindingResult, nonAdminUser);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Apenas usuários com cargo de ADMINISTRADOR podem salvar profissionais.", response.getBody());
    }

    // @Test
    // void testDeletarProfissionalComSucesso() {
    // Profissional profissional = new Profissional();
    // profissional.setIdProfissional(1L);
    // profissional.setNome("Dr. João");

    // UserDetails adminUser = User.withUsername("admin")
    // .password("password")
    // .authorities(new SimpleGrantedAuthority("ADMINISTRADOR"))
    // .build();

    // when(profissionalRepository.findById(1L)).thenReturn(Optional.of(profissional));

    // ResponseEntity<String> response = profissionalService.deletarProfissional(1L,
    // adminUser);

    // assertEquals(HttpStatus.OK, response.getStatusCode());
    // assertEquals("Profissional deletado com sucesso", response.getBody());
    // }

    @Test
    void testDeletarProfissionalSemPermissao() {
        UserDetails nonAdminUser = User.withUsername("user")
                .password("password")
                .authorities(new SimpleGrantedAuthority("USER"))
                .build();

        ResponseEntity<String> response = profissionalService.deletarProfissional(1L, nonAdminUser);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Apenas usuários com cargo de ADMINISTRADOR podem excluir profissionais.", response.getBody());
    }
}

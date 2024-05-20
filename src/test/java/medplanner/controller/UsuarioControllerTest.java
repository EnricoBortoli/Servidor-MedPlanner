package medplanner.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import medplanner.model.Usuario;
import medplanner.repository.UsuarioRepository;
import medplanner.services.TokenService;
import medplanner.validation.CPFValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.Collections;

class UsuarioControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private TokenService tokenService;

    @Mock
    private CPFValidator cpfValidator;

    @InjectMocks
    private UsuarioController usuarioController;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(usuarioController).build();
    }

    @Test
    void testListarUsuario() throws Exception {
        when(usuarioRepository.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/usuario/listar"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$").isArray())
               .andExpect(jsonPath("$").isEmpty());
    }

    /*@Test
    void testSalvarUsuario() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setUsername("new.email@example.com");
        usuario.setPassword("password");
        usuario.setNome("Nome Valido");
        usuario.setCpf("70490670083");
        usuario.setCargo(Usuario.Cargo.ADMINISTRADOR);
        usuario.setSituacao("A");

        when(cpfValidator.isValid(anyString(), any())).thenReturn(true);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        mockMvc.perform(post("/usuario/salvar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isOk());
    }

    @Test
    void testDeletarUsuarioUnauthorized() throws Exception {
        mockMvc.perform(delete("/usuario/deletar/1"))
               .andExpect(status().isForbidden());
        }

    @Test
    void testLogin() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setUsername("valid.email@example.com");
        usuario.setPassword("password");

        mockMvc.perform(post("/usuario/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isBadRequest());
    }*/

}

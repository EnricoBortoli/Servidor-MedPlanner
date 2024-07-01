package medplanner.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import medplanner.model.Ala;
import medplanner.model.Sala;
import medplanner.services.SalaService;

@WebMvcTest(SalaController.class)
public class SalaControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private SalaService salaService;

  @InjectMocks
  private SalaController salaController;

  private ObjectMapper objectMapper = new ObjectMapper();

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  @WithMockUser(roles = { "ADMINISTRADOR", "RECEPCAO" })
  void testSalvarSala() throws Exception {
    Sala sala = new Sala();
    sala.setNomeSala("Sala 1");
    Ala ala = new Ala();
    ala.setIdAla(1L);
    sala.setAla(ala);
    sala.setAndar(1);
    sala.setSituacao("A");

    when(salaService.salvarSala(sala)).thenReturn(sala);

    mockMvc.perform(post("/sala/salvar")
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(sala)))
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(roles = { "ADMINISTRADOR", "RECEPCAO" })
  void testDeletarSala() throws Exception {
    Long salaId = 1L;

    mockMvc.perform(delete("/sala/deletar/{id}", salaId))
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(roles = { "ADMINISTRADOR", "RECEPCAO" })
  void testBuscarSalas() throws Exception {
    mockMvc.perform(get("/sala/buscar"))
        .andExpect(status().isOk());
  }

  @Test
  void testSalvarSalaForbidden() throws Exception {
    Sala sala = new Sala();
    sala.setNomeSala("Sala 1");
    Ala ala = new Ala();
    ala.setIdAla(1L);
    sala.setAla(ala);
    sala.setAndar(1);
    sala.setSituacao("A");

    mockMvc.perform(post("/sala/salvar")
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(sala)))
        .andExpect(status().isForbidden());
  }

  @Test
  void testDeletarSalaForbidden() throws Exception {
    Long salaId = 1L;

    mockMvc.perform(delete("/sala/deletar/{id}", salaId))
        .andExpect(status().isForbidden());
  }
}

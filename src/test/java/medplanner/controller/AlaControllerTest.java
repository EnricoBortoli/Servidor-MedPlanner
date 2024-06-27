package medplanner.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;

import medplanner.model.Ala;
import medplanner.services.AlaService;

public class AlaControllerTest {

    @Mock
    private AlaService alaService;

    @InjectMocks
    private AlaController alaController;

    private MockMvc mockMvc;

    private Ala ala;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(alaController).build();
        ala = new Ala();
        ala.setIdAla(1L);
        ala.setNome("Test Ala");
        ala.setSigla("TST");
    }

    @Test
    public void testSalvarAla() throws Exception {
        when(alaService.validateAla(any(Ala.class), any(BindingResult.class))).thenReturn(Collections.emptyList());
        when(alaService.saveAla(any(Ala.class))).thenReturn(ala);

        mockMvc.perform(post("/ala/salvar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Test Ala\",\"sigla\":\"TST\",\"andar\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Test Ala"))
                .andExpect(jsonPath("$.sigla").value("TST"));
    }

    @Test
    public void testListarAlas() throws Exception {
        when(alaService.getAllAlas()).thenReturn(Collections.singletonList(ala));

        mockMvc.perform(get("/ala/listar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Test Ala"))
                .andExpect(jsonPath("$[0].sigla").value("TST"));
    }

    @Test
    public void testBuscarAlaById() throws Exception {
        when(alaService.getAlaById(1L)).thenReturn(Optional.of(ala));

        mockMvc.perform(get("/ala/buscar/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Test Ala"))
                .andExpect(jsonPath("$.sigla").value("TST"));
    }

    @Test
    public void testDeleteAla() throws Exception {
        when(alaService.getAlaById(1L)).thenReturn(Optional.of(ala));
        doNothing().when(alaService).deleteAla(any(Ala.class));

        mockMvc.perform(delete("/ala/deletar/{id}", 1L))
                .andExpect(status().isOk());
    }
}

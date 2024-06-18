package medplanner.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import medplanner.model.Ala;
import medplanner.repository.AlaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AlaController.class)
public class AlaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AlaRepository alaRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Ala ala;

    @BeforeEach
    void setUp() {
        ala = new Ala();
        ala.setIdAla(1L);
        ala.setNome("Ala Norte");
        ala.setSigla("AN");
        ala.setAndar(1);
    }

    @Test
    void createAla_shouldReturnCreatedAla() throws Exception {
        given(alaRepository.save(any(Ala.class))).willReturn(ala);

        mockMvc.perform(post("/ala")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ala)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idAla").value(ala.getIdAla()))
                .andExpect(jsonPath("$.nome").value(ala.getNome()))
                .andExpect(jsonPath("$.sigla").value(ala.getSigla()))
                .andExpect(jsonPath("$.andar").value(ala.getAndar()));
    }

    @Test
    void createAla_shouldReturnBadRequestForInvalidAla() throws Exception {
        Ala invalidAla = new Ala();
        invalidAla.setNome(""); // Nome inválido
        invalidAla.setSigla("AN123456789"); // Sigla inválida
        invalidAla.setAndar(-1); // Andar inválido

        mockMvc.perform(post("/ala")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidAla)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0]").value("Nome é obrigatório"))
                .andExpect(jsonPath("$[1]").value("Sigla deve ter no máximo 10 caracteres"))
                .andExpect(jsonPath("$[2]").value("Andar deve ser um número positivo"));
    }

    @Test
    void getAllAlas_shouldReturnAlas() throws Exception {
        given(alaRepository.findAll()).willReturn(Collections.singletonList(ala));

        mockMvc.perform(get("/ala")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idAla").value(ala.getIdAla()))
                .andExpect(jsonPath("$[0].nome").value(ala.getNome()))
                .andExpect(jsonPath("$[0].sigla").value(ala.getSigla()))
                .andExpect(jsonPath("$[0].andar").value(ala.getAndar()));
    }

    @Test
    void getAlaById_shouldReturnAla() throws Exception {
        given(alaRepository.findById((long) anyInt())).willReturn(Optional.of(ala));

        mockMvc.perform(get("/ala/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idAla").value(ala.getIdAla()))
                .andExpect(jsonPath("$.nome").value(ala.getNome()))
                .andExpect(jsonPath("$.sigla").value(ala.getSigla()))
                .andExpect(jsonPath("$.andar").value(ala.getAndar()));
    }

    @Test
    void getAlaById_shouldReturnNotFound() throws Exception {
        given(alaRepository.findById((long) anyInt())).willReturn(Optional.empty());

        mockMvc.perform(get("/ala/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateAla_shouldReturnUpdatedAla() throws Exception {
        given(alaRepository.findById((long) anyInt())).willReturn(Optional.of(ala));
        given(alaRepository.save(any(Ala.class))).willReturn(ala);

        ala.setNome("Ala Sul");

        mockMvc.perform(put("/ala/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ala)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idAla").value(ala.getIdAla()))
                .andExpect(jsonPath("$.nome").value(ala.getNome()))
                .andExpect(jsonPath("$.sigla").value(ala.getSigla()))
                .andExpect(jsonPath("$.andar").value(ala.getAndar()));
    }

    @Test
    void updateAla_shouldReturnBadRequestForInvalidAla() throws Exception {
        Ala invalidAla = new Ala();
        invalidAla.setNome(""); // Nome inválido
        invalidAla.setSigla("AN123456789"); // Sigla inválida
        invalidAla.setAndar(-1); // Andar inválido

        mockMvc.perform(put("/ala/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidAla)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0]").value("Nome é obrigatório"))
                .andExpect(jsonPath("$[1]").value("Sigla deve ter no máximo 10 caracteres"))
                .andExpect(jsonPath("$[2]").value("Andar deve ser um número positivo"));
    }

    @Test
    void deleteAla_shouldReturnOk() throws Exception {
        given(alaRepository.findById((long) anyInt())).willReturn(Optional.of(ala));

        mockMvc.perform(delete("/ala/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void deleteAla_shouldReturnNotFound() throws Exception {
        given(alaRepository.findById((long) anyInt())).willReturn(Optional.empty());

        mockMvc.perform(delete("/ala/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}

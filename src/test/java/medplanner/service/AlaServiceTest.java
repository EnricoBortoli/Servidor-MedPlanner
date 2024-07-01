package medplanner.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import medplanner.model.Ala;
import medplanner.repository.AlaRepository;
import medplanner.services.AlaService;

public class AlaServiceTest {

    @Mock
    private AlaRepository alaRepository;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private AlaService alaService;

    private Ala ala;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        ala = new Ala();
        ala.setIdAla(1L);
        ala.setNome("Ala Norte");
        ala.setSigla("AN");
    }

    @Test
    public void testValidateAla_NoErrors() {
        when(bindingResult.hasErrors()).thenReturn(false);
        when(alaRepository.findByNome(ala.getNome())).thenReturn(Optional.empty());
        when(alaRepository.findBySigla(ala.getSigla())).thenReturn(Optional.empty());

        List<String> errors = alaService.validateAla(ala, bindingResult);
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testValidateAla_WithErrors() {
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getAllErrors()).thenReturn(Collections.singletonList(new ObjectError("Ala", "Validation error")));

        List<String> errors = alaService.validateAla(ala, bindingResult);
        assertFalse(errors.isEmpty());
        assertEquals(1, errors.size());
        assertEquals("Validation error", errors.get(0));
    }

    @Test
    public void testSaveAla() {
        when(alaRepository.save(ala)).thenReturn(ala);
        Ala savedAla = alaService.saveAla(ala);
        assertNotNull(savedAla);
        assertEquals(ala, savedAla);
    }

    @Test
    public void testGetAlaById() {
        when(alaRepository.findById(1L)).thenReturn(Optional.of(ala));
        Optional<Ala> foundAla = alaService.getAlaById(1L);
        assertTrue(foundAla.isPresent());
        assertEquals(ala, foundAla.get());
    }

    @Test
    public void testGetAllAlas() {
        List<Ala> alas = Collections.singletonList(ala);
        when(alaRepository.findAll()).thenReturn(alas);
        List<Ala> foundAlas = alaService.getAllAlas();
        assertNotNull(foundAlas);
        assertEquals(1, foundAlas.size());
        assertEquals(ala, foundAlas.get(0));
    }

    @Test
    public void testDeleteAla() {
        doNothing().when(alaRepository).delete(ala);
        alaService.deleteAla(ala);
        verify(alaRepository, times(1)).delete(ala);
    }

    @Test
    public void testUpdateAla() {
        Ala updatedAlaDetails = new Ala();
        updatedAlaDetails.setNome("Ala TESTE");
        updatedAlaDetails.setSigla("AT");

        when(alaRepository.save(any(Ala.class))).thenReturn(updatedAlaDetails);
        Ala updatedAla = alaService.updateAla(updatedAlaDetails, ala);
        assertNotNull(updatedAla);
        assertEquals("Ala TESTE", updatedAla.getNome());
        assertEquals("AT", updatedAla.getSigla());
    }
}

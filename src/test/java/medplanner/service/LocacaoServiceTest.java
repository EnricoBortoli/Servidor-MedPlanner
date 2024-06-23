package medplanner.service;

import medplanner.model.Locacao;
import medplanner.model.Sala;
import medplanner.repository.LocacaoRepository;
import medplanner.services.LocacaoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class LocacaoServiceTest {

    @Mock
    private LocacaoRepository locacaoRepository;

    @InjectMocks
    private LocacaoService locacaoService;

    private Locacao locacao;
    private Sala sala;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        sala = new Sala();
        sala.setId(1L);

        locacao = new Locacao();
        locacao.setIdLocacao(1L);
        locacao.setSala(sala);
        locacao.setHoraInicio(LocalDateTime.of(2024, 6, 18, 10, 0));
        locacao.setHoraFinal(LocalDateTime.of(2024, 6, 18, 11, 0));
        locacao.setData(new Date());
    }

    @Test
    public void testSalvarLocacaoComSucesso() {
        when(locacaoRepository.existeDataHoraMarcadaNaSala(any(Sala.class), any(LocalDateTime.class), any(LocalDateTime.class), any(Date.class))).thenReturn(false);
        when(locacaoRepository.save(any(Locacao.class))).thenReturn(locacao);

        Locacao savedLocacao = locacaoService.salvarLocacao(locacao);

        assertNotNull(savedLocacao);
        assertEquals(locacao.getIdLocacao(), savedLocacao.getIdLocacao());
    }

    @Test
    public void testSalvarLocacaoComConflito() {
        when(locacaoRepository.existeDataHoraMarcadaNaSala(any(Sala.class), any(LocalDateTime.class), any(LocalDateTime.class), any(Date.class))).thenReturn(true);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            locacaoService.salvarLocacao(locacao);
        });

        assertEquals("Atenção! Já está registrado uma locação para a sala, data e horário informados!", exception.getMessage());
    }

    @Test
    public void testAtualizarLocacaoComSucesso() {
        when(locacaoRepository.findById(anyLong())).thenReturn(Optional.of(locacao));
        when(locacaoRepository.existeDataHoraMarcadaNaSala(anyLong(), any(Sala.class), any(LocalDateTime.class), any(LocalDateTime.class), any(Date.class))).thenReturn(false);
        when(locacaoRepository.save(any(Locacao.class))).thenReturn(locacao);

        Locacao updatedLocacao = locacaoService.atualizarLocacao(locacao.getIdLocacao(), locacao);

        assertNotNull(updatedLocacao);
        assertEquals(locacao.getIdLocacao(), updatedLocacao.getIdLocacao());
    }

    @Test
    public void testAtualizarLocacaoComConflito() {
        when(locacaoRepository.findById(anyLong())).thenReturn(Optional.of(locacao));
        when(locacaoRepository.existeDataHoraMarcadaNaSala(anyLong(), any(Sala.class), any(LocalDateTime.class), any(LocalDateTime.class), any(Date.class))).thenReturn(true);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            locacaoService.atualizarLocacao(locacao.getIdLocacao(), locacao);
        });

        assertEquals("Atenção! Já está registrado uma locação para a sala, data e horário informados!", exception.getMessage());
    }

    @Test
    public void testListarLocacoes() {
        when(locacaoRepository.findAll()).thenReturn(Arrays.asList(locacao));

        List<Locacao> locacoes = locacaoService.listarLocacoes();

        assertNotNull(locacoes);
        assertEquals(1, locacoes.size());
        assertEquals(locacao.getIdLocacao(), locacoes.get(0).getIdLocacao());
    }

    @Test
    public void testBuscarLocacaoByIdComSucesso() {
        when(locacaoRepository.findById(anyLong())).thenReturn(Optional.of(locacao));

        Optional<Locacao> foundLocacao = locacaoService.buscarLocacaoById(locacao.getIdLocacao());

        assertTrue(foundLocacao.isPresent());
        assertEquals(locacao.getIdLocacao(), foundLocacao.get().getIdLocacao());
    }

    @Test
    public void testBuscarLocacaoByIdNaoEncontrado() {
        when(locacaoRepository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<Locacao> foundLocacao = locacaoService.buscarLocacaoById(2L);

        assertFalse(foundLocacao.isPresent());
    }

    @Test
    public void testDeletarLocacaoComSucesso() {
        when(locacaoRepository.findById(anyLong())).thenReturn(Optional.of(locacao));
        doNothing().when(locacaoRepository).delete(any(Locacao.class));

        assertDoesNotThrow(() -> locacaoService.deletarLocacao(locacao.getIdLocacao()));

        verify(locacaoRepository, times(1)).delete(locacao);
    }

    @Test
    public void testDeletarLocacaoNaoEncontrado() {
        when(locacaoRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            locacaoService.deletarLocacao(2L);
        });

        assertEquals("Locação não encontrada!", exception.getMessage());
    }
}

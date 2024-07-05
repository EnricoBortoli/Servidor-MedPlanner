package medplanner.service;

import medplanner.dto.LocacaoDTO;
import medplanner.model.Ala;
import medplanner.model.Locacao;
import medplanner.model.Sala;
import medplanner.model.Usuario;
import medplanner.repository.AlaRepository;
import medplanner.repository.LocacaoRepository;
import medplanner.repository.SalaRepository;
import medplanner.repository.UsuarioRepository;
import medplanner.services.LocacaoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class LocacaoServiceTest {

    @InjectMocks
    private LocacaoService locacaoService;

    @Mock
    private LocacaoRepository locacaoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private SalaRepository salaRepository;

    @Mock
    private AlaRepository alaRepository;

    private Usuario usuario;
    private Sala sala;
    private Ala ala;
    private Locacao locacao;
    private LocacaoDTO locacaoDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        usuario = new Usuario();
        usuario.setIdUsuario(2L);
        usuario.setCargo(Usuario.Cargo.MEDICO);

        sala = new Sala();
        sala.setIdSala(1L);

        ala = new Ala();
        ala.setIdAla(1L);

        locacao = new Locacao();
        locacao.setIdLocacao(1L);
        locacao.setUsuario(usuario);
        locacao.setSala(sala);
        locacao.setAla(ala);
        locacao.setDia(new Date());
        locacao.setHoraInicio(LocalDateTime.now().plusHours(1));
        locacao.setHoraFinal(LocalDateTime.now().plusHours(2));

        locacaoDTO = new LocacaoDTO();
        locacaoDTO.setIdLocacao(1L);
        locacaoDTO.setIdUsuario(1L);
        locacaoDTO.setSala(1L);
        locacaoDTO.setAla(1L);
        locacaoDTO.setDia(new Date());
        locacaoDTO.setHoraInicio(LocalDateTime.now().plusHours(1));
        locacaoDTO.setHoraFinal(LocalDateTime.now().plusHours(2));
    }

    @Test
    void salvarLocacaoTest() {
        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.of(usuario));
        when(salaRepository.findById(anyLong())).thenReturn(Optional.of(sala));
        when(alaRepository.findById(anyLong())).thenReturn(Optional.of(ala));
        when(locacaoRepository.save(any(Locacao.class))).thenReturn(locacao);

        Locacao savedLocacao = locacaoService.verificarUsuarioAntesDeSalvar(usuario.getIdUsuario(), locacaoDTO);

        assertNotNull(savedLocacao);
        assertEquals(locacao.getIdLocacao(), savedLocacao.getIdLocacao());
        verify(locacaoRepository, times(1)).save(any(Locacao.class));
    }

    @Test
    void atualizarLocacaoTest() {
        when(locacaoRepository.findById(anyLong())).thenReturn(Optional.of(locacao));
        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.of(usuario));
        when(salaRepository.findById(anyLong())).thenReturn(Optional.of(sala));
        when(alaRepository.findById(anyLong())).thenReturn(Optional.of(ala));
        when(locacaoRepository.save(any(Locacao.class))).thenReturn(locacao);

        Locacao updatedLocacao = locacaoService.atualizarLocacao(usuario.getIdUsuario(), locacaoDTO);

        assertNotNull(updatedLocacao);
        assertEquals(locacao.getIdLocacao(), updatedLocacao.getIdLocacao());
        verify(locacaoRepository, times(1)).save(any(Locacao.class));
    }

    // @Test
    // void deletarLocacaoTest() {
    //     when(locacaoRepository.findById(anyLong())).thenReturn(Optional.of(locacao));

    //     locacaoService.deletarLocacao(locacao.getIdLocacao());

    //     verify(locacaoRepository, times(1)).delete(locacao);
    // }

    @Test
    void listarLocacoesTest() {
        locacaoService.listarLocacoes();

        verify(locacaoRepository, times(1)).findAll();
    }

    @Test
    void buscarLocacaoByIdTest() {
        when(locacaoRepository.findById(anyLong())).thenReturn(Optional.of(locacao));

        Optional<Locacao> foundLocacao = locacaoService.buscarLocacaoById(locacao.getIdLocacao());

        assertTrue(foundLocacao.isPresent());
        assertEquals(locacao.getIdLocacao(), foundLocacao.get().getIdLocacao());
    }
}

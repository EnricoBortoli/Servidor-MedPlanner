package medplanner.services;

import medplanner.dto.LocacaoDTO;
import medplanner.model.Locacao;
import medplanner.model.Usuario;
import medplanner.repository.LocacaoRepository;
import medplanner.repository.UsuarioRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class LocacaoService {

    @Autowired
    private LocacaoRepository locacaoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    // Metodo principal de gravação
    public Locacao salvarLocacaoGeral(Usuario usuario, LocacaoDTO locacaoDetails) {
        if (locacaoRepository.existeDataHoraMarcadaNaSala(locacaoDetails.getSala(), locacaoDetails.getHoraInicio(),
                locacaoDetails.getHoraFinal(), locacaoDetails.getData())) {
            throw new IllegalArgumentException("Atenção! Já está registrado uma locação para a sala, data e horário informados!");
        }

        Locacao locacao = new Locacao();
        locacao.setUsuario(usuario);
        BeanUtils.copyProperties(locacaoDetails, locacao);
        return locacaoRepository.save(locacao);
    }

    // Utiliza o usuário atual autenticado
    public Locacao salvarLocacao(LocacaoDTO locacaoDetails) {
        Usuario currentUser = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return salvarLocacaoGeral(currentUser, locacaoDetails);
    }

    // Salva o usuário por id
    public Locacao salvarLocacao(Long usuarioId, LocacaoDTO locacaoDetails) {
        Usuario user = usuarioRepository.findById(usuarioId).orElseThrow();
        return salvarLocacaoGeral(user, locacaoDetails);
    }

    public Locacao atualizarLocacao(Long id, LocacaoDTO locacaoDetails) {
        Optional<Locacao> locacaoOptional = locacaoRepository.findById(id);
        if (!locacaoOptional.isPresent()) {
            throw new IllegalArgumentException("Locação não encontrada!");
        }

        Locacao locacao = locacaoOptional.get();

        if (locacaoRepository.existeDataHoraMarcadaNaSala(locacao.getIdLocacao(), locacaoDetails.getSala(),
                locacaoDetails.getHoraInicio(), locacaoDetails.getHoraFinal(), locacaoDetails.getData())) {
            throw new IllegalArgumentException("Atenção! Já está registrado uma locação para a sala, data e horário informados!");
        }

        BeanUtils.copyProperties(locacaoDetails, locacao);

        return locacaoRepository.save(locacao);
    }

    public List<Locacao> listarLocacoes() {
        return locacaoRepository.findAll();
    }

    public Optional<Locacao> buscarLocacaoById(Long id) {
        return locacaoRepository.findById(id);
    }

    public void deletarLocacao(Long id) {
        Optional<Locacao> locacaoOptional = locacaoRepository.findById(id);
        if (!locacaoOptional.isPresent()) {
            throw new IllegalArgumentException("Locação não encontrada!");
        }

        locacaoRepository.delete(locacaoOptional.get());
    }
}

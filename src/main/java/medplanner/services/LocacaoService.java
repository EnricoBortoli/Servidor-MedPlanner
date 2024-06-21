package medplanner.services;

import medplanner.model.Locacao;
import medplanner.model.Sala;
import medplanner.repository.LocacaoRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class LocacaoService {

    @Autowired
    private LocacaoRepository locacaoRepository;

    public Locacao salvarLocacao(Locacao locacaoDetails) {
        if (locacaoRepository.existeDataHoraMarcadaNaSala(locacaoDetails.getSala(), locacaoDetails.getHoraInicio(),
                locacaoDetails.getHoraFinal(), locacaoDetails.getData())) {
            throw new IllegalArgumentException("Atenção! Já está registrado uma locação para a sala, data e horário informados!");
        }

        return locacaoRepository.save(locacaoDetails);
    }

    public Locacao atualizarLocacao(Long id, Locacao locacaoDetails) {
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

package medplanner.services;

import medplanner.dto.RelatorioDTO;
import medplanner.model.Locacao;
import medplanner.repository.LocacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RelatorioService {

    @Autowired
    private LocacaoRepository locacaoRepository;

    public List<RelatorioDTO> medicosPorSala(Long salaId, LocalDateTime dataInicio, LocalDateTime dataFim) {
        List<Locacao> locacoes = locacaoRepository.findBySalaIdAndPeriodo(salaId, dataInicio, dataFim);
    
        return locacoes.stream()
                .collect(Collectors.groupingBy(locacao -> locacao.getUsuario().getIdUsuario()))
                .entrySet().stream()
                .map(entry -> {
                    Long medicoId = entry.getKey();
                    String nomeMedico = entry.getValue().get(0).getUsuario().getNome();
                    long totalHoras = entry.getValue().stream()
                            .mapToLong(locacao -> Duration.between(locacao.getHoraInicio(), locacao.getHoraFinal()).toHours())
                            .sum();
                    return new RelatorioDTO(medicoId, nomeMedico, totalHoras);
                })
                .collect(Collectors.toList());
    }

    public List<RelatorioDTO> salasPorMedico(Long medicoId, LocalDateTime dataInicio, LocalDateTime dataFim) {
        List<Locacao> locacoes = locacaoRepository.findByMedicoIdAndPeriodo(medicoId, dataInicio, dataFim);

        return locacoes.stream()
                .collect(Collectors.groupingBy(locacao -> locacao.getSala().getIdSala()))
                .entrySet().stream()
                .map(entry -> {
                    Long salaId = entry.getKey();
                    String nomeSala = entry.getValue().get(0).getSala().getNomeSala();
                    Long totalHoras = entry.getValue().stream()
                            .mapToLong(locacao -> Duration.between(locacao.getHoraInicio(), locacao.getHoraFinal()).toHours())
                            .sum();
                    return new RelatorioDTO(salaId, nomeSala, totalHoras);
                })
                .collect(Collectors.toList());
    }
}

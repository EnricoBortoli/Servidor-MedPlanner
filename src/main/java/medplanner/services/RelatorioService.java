package medplanner.services;

import medplanner.dto.RelatorioDTO;
import medplanner.model.Locacao;
import medplanner.repository.LocacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RelatorioService {

    @Autowired
    private LocacaoRepository locacaoRepository;

//    public List<RelatorioDTO> medicosPorSala(Long salaId, LocalDateTime dataInicio, LocalDateTime dataFim) {
//        List<Locacao> locacoes = locacaoRepository.findBySalaIdAndPeriodo(salaId, dataInicio, dataFim);
//
//        return locacoes.stream()
//                .collect(Collectors.groupingBy(locacao -> locacao.getMedico().getId()))
//                .entrySet().stream()
//                .map(entry -> {
//                    Long medicoId = entry.getKey();
//                    String nomeMedico = entry.getValue().get(0).getMedico().getNome();
//                    Long totalHoras = entry.getValue().stream()
//                            .mapToLong(locacao -> locacao.getPeriodo().toHours())
//                            .sum();
//                    return new RelatorioDTO(medicoId, nomeMedico, totalHoras);
//                })
//                .collect(Collectors.toList());
//    }
//
//    public List<RelatorioDTO> salasPorMedico(Long medicoId, LocalDateTime dataInicio, LocalDateTime dataFim) {
//        List<Locacao> locacoes = locacaoRepository.findByMedicoIdAndPeriodo(medicoId, dataInicio, dataFim);
//
//        return locacoes.stream()
//                .collect(Collectors.groupingBy(locacao -> locacao.getSala().getId()))
//                .entrySet().stream()
//                .map(entry -> {
//                    Long salaId = entry.getKey();
//                    String nomeSala = entry.getValue().get(0).getSala().getNome();
//                    Long totalHoras = entry.getValue().stream()
//                            .mapToLong(locacao -> locacao.getPeriodo().toHours())
//                            .sum();
//                    return new RelatorioDTO(salaId, nomeSala, totalHoras);
//                })
//                .collect(Collectors.toList());
//    }
}

package medplanner.controller;

import medplanner.dto.RelatorioDTO;
import medplanner.model.Locacao;
import medplanner.services.LocacaoService;
import medplanner.services.RelatorioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/relatorios")
public class RelatorioController {

    @Autowired
    private RelatorioService relatorioService;

    @Autowired
    private LocacaoService locacaoServiceService;

    @GetMapping("/medicos-por-sala")
    public ResponseEntity<List<RelatorioDTO>> getMedicosPorSala(
            @RequestParam Long salaId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        List<RelatorioDTO> relatorio = relatorioService.medicosPorSala(salaId, dataInicio.atStartOfDay(), dataFim.atStartOfDay().plusDays(1));
        return ResponseEntity.ok(relatorio);
    }

    @GetMapping("/salas-por-medico")
    public ResponseEntity<List<RelatorioDTO>> getSalasPorMedico(
            @RequestParam Long medicoId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        List<RelatorioDTO> relatorio = relatorioService.salasPorMedico(medicoId, dataInicio.atStartOfDay(), dataFim.atStartOfDay().plusDays(1));
        return ResponseEntity.ok(relatorio);
    }

    @GetMapping("/diario")
    public ResponseEntity<List<Locacao>> getLocacoesDoDia() {
        // Obter a data atual sem a parte da hora
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date diaAtual = calendar.getTime();

        List<Locacao> relatorio = locacaoServiceService.listarLocacoesPorDia(diaAtual);
        return ResponseEntity.ok(relatorio);
    }
}

package medplanner.controller;

import medplanner.dto.RelatorioDTO;
import medplanner.services.RelatorioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/relatorios")
public class RelatorioController {

    @Autowired
    private RelatorioService relatorioService;

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
}

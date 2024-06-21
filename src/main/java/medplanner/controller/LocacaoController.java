package medplanner.controller;

import medplanner.model.Locacao;
import medplanner.repository.LocacaoRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/locacao")
public class LocacaoController {

    @Autowired
    private LocacaoRepository locacaoRepository;

    @PostMapping("/salvar")
    public ResponseEntity<?> salvarLocacao(@PathVariable Long id, @Valid @RequestBody Locacao locacaoDetails, BindingResult result) {

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }

        Optional<Locacao> locacaoOptional = locacaoRepository.findById(id);
        if (locacaoOptional.isEmpty()) {
            if (this.locacaoRepository.existeDataHoraMarcadaNaSala(locacaoDetails.getSala(), locacaoDetails.getHoraInicio(), locacaoDetails.getHoraFinal(),
                    locacaoDetails.getData())) {
                return ResponseEntity.badRequest().body("Atenção! Já está registrado uma locação para a sala, data e horário informados!");
            }

            Locacao locacao = new Locacao();
            BeanUtils.copyProperties(locacaoDetails, locacao);
            return ResponseEntity.ok(locacao);
        } else {
            Locacao locacao = locacaoOptional.get();

            if (this.locacaoRepository.existeDataHoraMarcadaNaSala(locacao.getIdLocacao(),
                    locacaoDetails.getSala(), locacaoDetails.getHoraInicio(), locacaoDetails.getHoraFinal(),
                    locacaoDetails.getData())) {
                return ResponseEntity.badRequest().body("Atenção! Já está registrado uma locação para a sala, data e horário informados!");
            }

            BeanUtils.copyProperties(locacaoDetails, locacao);
            return ResponseEntity.ok(locacao);
        }
    }

    @GetMapping("/listar")
    public List<Locacao> listarLocacoes() {
        return locacaoRepository.findAll();
    }

    @GetMapping("/buscar")
    public ResponseEntity<?> buscarLocacaoById(@PathVariable Long id) {
        Optional<Locacao> locacao = locacaoRepository.findById(id);
        return locacao.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteLocacao(@PathVariable Long id) {
        Optional<Locacao> locacao = locacaoRepository.findById(id);
        if (!locacao.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        locacaoRepository.delete(locacao.get());
        return ResponseEntity.ok().build();
    }
}

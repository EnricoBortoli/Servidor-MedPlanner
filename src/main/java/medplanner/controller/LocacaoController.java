package medplanner.controller;

import medplanner.model.Locacao;
import medplanner.repository.LocacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/locacao")
public class LocacaoController {

    @Autowired
    private LocacaoRepository locacaoRepository;

    @PostMapping("/criar")
    public ResponseEntity<?> criarLocacao(@Valid @RequestBody Locacao locacao, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
//
//        boolean exists = locacaoRepository.existeDataHoraMarcadaNaSala(
//                locacao.getData(), locacao.getHoraInicio(), locacao.getHoraFinal()
//        ); // , locacao.getSala()

        //fazer a verificação para cada variavel para fazer a locação
//        if (exists) {
//            return ResponseEntity.badRequest().body("Locação já existe para a data, hora e sala selecionados.");
//        }

        Locacao savedLocacao = locacaoRepository.save(locacao);
        return ResponseEntity.ok(savedLocacao);
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

    @PutMapping("/salvar")
    public ResponseEntity<?> salvarLocacao(@PathVariable Long id, @Valid @RequestBody Locacao locacaoDetails, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }

        Optional<Locacao> locacaoOptional = locacaoRepository.findById(id);
        if (!locacaoOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Locacao locacao = locacaoOptional.get();
        locacao.setHoraInicio(locacaoDetails.getHoraInicio());
        locacao.setHoraFinal(locacaoDetails.getHoraFinal());
        locacao.setData(locacaoDetails.getData());
//        locacao.setAla(locacaoDetails.getAla());
//        locacao.setProfissional(locacaoDetails.getProfissional());
//        locacao.setSala(locacaoDetails.getSala());

//        boolean exists = locacaoRepository.existeDataHoraMarcadaNaSala(
//                locacao.getData(), locacao.getHoraInicio(), locacao.getHoraFinal()
//        ); // , locacao.getSala()

//        if (exists) {
//            return ResponseEntity.badRequest().body("Locação já existe para a data, hora e sala especificados.");
//        }

        Locacao updatedLocacao = locacaoRepository.save(locacao);
        return ResponseEntity.ok(updatedLocacao);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteLocacao(@PathVariable Long id) {
        Optional<Locacao> locacao = locacaoRepository.findById(id);
        if (!locacao.isPresent()) {
            return ResponseEntity.notFound().build();
        }

//        boolean hasFutureBookings = locacaoRepository.existsByDataAndHoraInicioAfter(new Date());

//        if (hasFutureBookings) {
//            return ResponseEntity.badRequest().body("Não é possível deletar a locação pois há locações futuras marcadas.");
//        }

        locacaoRepository.delete(locacao.get());
        return ResponseEntity.ok().build();
    }
}

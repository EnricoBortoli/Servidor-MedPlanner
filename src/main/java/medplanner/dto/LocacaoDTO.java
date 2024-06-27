package medplanner.dto;


import lombok.Getter;
import lombok.Setter;


import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
public class LocacaoDTO {
        private Long idLocacao;
        private Long idUsuario;
        private LocalDateTime horaInicio;
        private Date data;
        private LocalDateTime horaFinal;
        private SalaDTO sala;
}

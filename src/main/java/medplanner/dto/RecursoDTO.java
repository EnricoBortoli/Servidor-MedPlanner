package medplanner.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecursoDTO {
  private Long idRecurso;
  private String nomeRecurso;
  private String descricao;
  private Long idSala;

}

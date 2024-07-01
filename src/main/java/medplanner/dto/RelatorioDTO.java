package medplanner.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RelatorioDTO {
    private Long id;
    private String nome;
    private Long totalHoras;

    public RelatorioDTO() {}

    public RelatorioDTO(Long id, String nome, Long totalHoras) {
        this.id = id;
        this.nome = nome;
        this.totalHoras = totalHoras;
    }
}

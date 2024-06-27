package medplanner.dto;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Long getTotalHoras() {
        return totalHoras;
    }

    public void setTotalHoras(Long totalHoras) {
        this.totalHoras = totalHoras;
    }
}

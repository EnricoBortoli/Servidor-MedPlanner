package medplanner.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "especialidade")
public class Especialidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEspecialidade;

    @Column(nullable = false)
    @NotEmpty(message = "O nome é obrigatório")
    @Size(min = 2, message = "O nome precisa ter mais de 2 caracteres")
    @Size(max = 200, message = "O nome deve ter no máximo 200 caracteres")
    private String nome;

    @Column(nullable = false, length = 2)
    @NotEmpty(message = "A sigla é obrigatória")
    @Size(min = 2, max = 2, message = "A sigla deve ter 2 caracteres")
    private String sigla;

    public Long getIdEspecialidade() {
        return idEspecialidade;
    }

    public void setIdEspecialidade(Long idEspecialidade) {
        this.idEspecialidade = idEspecialidade;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }
}

package medplanner.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Ala")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "idAla")
public class Ala {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAla;

    @Column()
    @Size(max = 50, message = "O nome da Ala deve ter no máximo 50 caracteres!")
    @NotEmpty(message = "O nome da Ala é obrigatório!")
    private String nome;

    @Column()
    @Size(max = 3, message = "A sigla deve ter no máximo 3 caracteres!")
    @NotEmpty(message = "A sigla da Ala é obrigatório!")
    private String sigla;
}

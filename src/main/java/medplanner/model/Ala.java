package medplanner.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "Ala")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of="idAla")
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

    @Column()
    private Integer andar;

}

package medplanner.model;

import jakarta.annotation.Nonnull;
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
@Table(name = "recursos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of="idRecurso")
public class Recurso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRecurso;

    @Column()
    @Nonnull()
    @NotEmpty(message = "O nome do recurso é o é obrigatório")
    @Size(min = 2, message = "O nome do recurso precisa ter mais de 2 caracteres")
    @Size(max = 100, message = "O nome do recurso deve ter no máximo 100 caracteres")
    private String nomeRecurso;

 
    @Column()
    @NotEmpty(message = "A descrição é obrigatória")
    @Size(max = 1, message = "A descrição deve ter no máximo 1 caracteres")
    private String descricao;
    
}

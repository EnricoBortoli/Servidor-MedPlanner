package medplanner.model;

import java.util.List;

import jakarta.annotation.Nonnull;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "sala")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "idSala")
public class Sala {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSala;

    @Column()
    @Nonnull()
    @NotEmpty(message = "O nome da sala é obrigatório")
    @Size(min = 2, message = "O nome da sala precisa ter mais de 2 caracteres")
    @Size(max = 50, message = "O nome da sala deve ter no máximo 50 caracteres")
    private String nomeSala;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_ala")
    private Ala ala;

    @Column()
    @Nonnull()
    private Integer andar;

    /**
     * A - Ativo
     * M - Manutenção
     * I - Inativo
     */
    @Column()
    @NotEmpty(message = "A situação é obrigatória")
    @Size(max = 1, message = "A situação deve ter no máximo 1 caracteres")
    private String situacao;

    @OneToMany(mappedBy = "sala", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Recurso> recursos;

}

package medplanner.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "Locacao")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of="id_locacao")
public class Locacao {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idLocacao;

    @Column()
    @NotEmpty(message = "A hora inicial é obrigatória!")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime horaInicio;

    @Column()
    @NotEmpty(message = "A data é obrigatória!")
    @Temporal(TemporalType.TIMESTAMP)
    private Date data;

    @Column()
    @NotEmpty(message = "A hora final é obrigatória!")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime horaFinal;

    @ManyToOne
    @NotEmpty(message = "O nome do médico é obrigatório!")
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @ManyToOne
    @NotEmpty(message = "O nome da sala é obrigatório!")
    @JoinColumn(name = "idSala")
    private Sala sala;

    @ManyToOne
    @NotEmpty(message = "O nome da ala é obrigatório!")
    @JoinColumn(name = "id_ala")
    private Ala ala;
}

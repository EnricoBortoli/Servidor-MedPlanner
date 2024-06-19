package medplanner.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "Locacao")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of="idLocacao")
public class Locacao {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idLocacao;

    @Column()
    @NotEmpty(message = "A hora inicial é obrigatória!")
    @Temporal(TemporalType.TIMESTAMP)
    private Date horaInicio;

    @Column()
    @NotEmpty(message = "A hora final é obrigatória!")
    @Temporal(TemporalType.TIMESTAMP)
    private Date horaFinal;

    @Column()
    @NotEmpty(message = "A data é obrigatória!")
    @Temporal(TemporalType.DATE)
    private Date data;

    @ManyToOne
    @NotEmpty(message = "O nome do médico é obrigatório!")
    @JoinColumn(name = "id_usuario")
    private Profissional profissional;

//    @ManyToOne
//    @NotEmpty(message = "O nome da sala é obrigatório!")
//    @JoinColumn(name = "idSala")
//    private Sala sala;
}

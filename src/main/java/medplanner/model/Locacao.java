package medplanner.model;

import jakarta.persistence.*;
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
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime horaInicio;

    @Column()
    @Temporal(TemporalType.TIMESTAMP)
    private Date dia;

    @Column()
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime horaFinal;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "idSala")
    private Sala sala;

    @ManyToOne
    @JoinColumn(name = "id_ala")
    private Ala ala;
}

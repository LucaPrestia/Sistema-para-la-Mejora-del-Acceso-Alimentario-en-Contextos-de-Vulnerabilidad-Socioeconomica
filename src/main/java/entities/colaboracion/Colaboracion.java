package entities.colaboracion;

import entities.PersistenciaID;
import entities.usuarios.Colaborador;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
@Entity
@Table(name = "colaboracion")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_colaboracion") // en la tabla colaboracion se crea un campo que va a discriminar una colaboracion de otra
public abstract class Colaboracion extends PersistenciaID {
    @ManyToOne // un colaborador = muchas colaboraciones
    @JoinColumn(name = "colaborador_id")
    private Colaborador colaborador;

}

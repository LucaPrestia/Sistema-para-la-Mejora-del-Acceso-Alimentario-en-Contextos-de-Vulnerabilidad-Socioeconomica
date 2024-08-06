package entities.heladera;

import entities.PersistenciaID;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "vianda")
public class Vianda extends PersistenciaID {

    private String comida;

    @Column(name = "fecha_caducidad")
    private Date fechaCaducidad;

    @Column(name = "fecha_donacion")
    private Date fechaDonacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "heladera_id")
    private Heladera heladera;

    private Float calorias;
    private Float peso;
    // private Estado estado;


}

package ar.utn.sistema.entities.incidente;

import ar.utn.sistema.entities.Direccion;
import ar.utn.sistema.entities.PersistenciaID;
import ar.utn.sistema.entities.heladera.Heladera;
import ar.utn.sistema.entities.notificacion.Notificacion;
import ar.utn.sistema.entities.usuarios.Tecnico;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter @Setter @NoArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipoIncidente") // Obligatorio para asignarle un nombre al campo que va a determinar de qué tipo de clase hija está conteniendo los datos
@Table(name = "Incidente")

public abstract class Incidente extends PersistenciaID {
    private LocalDateTime fechaHora;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "id_heladera")
    private Heladera heladera;

    private String estado; // ("PENDIENTE", "EN_PROGRESO", "RESUELTO")
    private String zona;
    public Incidente(LocalDateTime fechaHora, Heladera heladera, String tipoIncidente) {
        this.fechaHora = fechaHora;
        this.heladera = heladera;
        this.zona = heladera.getDireccion().getLocalidad();
        this.estado = "PENDIENTE";
    }

    public void notificarTecnico(Notificacion notificacion, List<Tecnico> tecnico){
        Direccion direccionHeladera = heladera.getDireccion();

        tecnico.forEach(x->x.notificar(notificacion));

    }
    public String getTexto(){
        return " Incidente ID: " +this.getId()+" De la Heladera:" +this.heladera.getNombre()+" Con ID: " + this.heladera.getId();
    }
}

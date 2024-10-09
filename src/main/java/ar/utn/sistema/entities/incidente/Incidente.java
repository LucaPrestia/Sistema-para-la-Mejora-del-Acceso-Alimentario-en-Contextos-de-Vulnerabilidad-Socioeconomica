package ar.utn.sistema.entities.incidente;

import ar.utn.sistema.entities.Direccion;
import ar.utn.sistema.entities.PersistenciaID;
import ar.utn.sistema.entities.heladera.Heladera;
import ar.utn.sistema.entities.notificacion.Notificacion;
import ar.utn.sistema.entities.usuarios.Tecnico;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor
public abstract class Incidente extends PersistenciaID {
    private LocalDateTime fechaHora;
    private Heladera heladera;
    private String estado; // ("PENDIENTE", "EN_PROGRESO", "RESUELTO")
    private String tipoIncidente; // "alerta" ; "falla_tecnica"

    public Incidente(LocalDateTime fechaHora, Heladera heladera, String tipoIncidente) {
        this.fechaHora = fechaHora;
        this.heladera = heladera;
        this.tipoIncidente = tipoIncidente;
        this.estado = "PENDIENTE";
    }

    public void notificarTecnico(Notificacion notificacion){
        // todo: notificar al técnico más cercano a la heladera donde ocurrio la falla tecnica
        Direccion direccionHeladera = heladera.getDireccion();
        Tecnico tecnico = new Tecnico(); // obtenerTecnicoMasCercano(direccionHeladera); esto quizás debería estar en el service de tecnico!!
        tecnico.notificar(notificacion);
    }
}

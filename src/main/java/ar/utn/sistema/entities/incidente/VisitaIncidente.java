package ar.utn.sistema.entities.incidente;

import ar.utn.sistema.entities.PersistenciaID;
import ar.utn.sistema.entities.heladera.EstadoHeladera;
import ar.utn.sistema.entities.usuarios.Tecnico;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor
public class VisitaIncidente extends PersistenciaID {
    // un incidente puede tener varias visitas de distintos técnicos y a su vez un técnico puede visitar distintos incidentes
    private Incidente incidente;
    private Tecnico tecnico;
    private LocalDate fechaVisita;
    private String descripcionTrabajo;
    private byte[] foto;
    private boolean solucionado = false;

    public VisitaIncidente(Incidente incidente, Tecnico tecnico, String descripcionTrabajo, byte[] foto, boolean solucionado) {
        this.incidente = incidente;
        this.tecnico = tecnico;
        this.descripcionTrabajo = descripcionTrabajo;
        this.foto = foto;
        this.fechaVisita = LocalDate.now();
        this.solucionado = solucionado;
        if (solucionado) incidente.getHeladera().setEstado(EstadoHeladera.ACTIVA); // si se soluciono la heladera vuelve a estar en funcionamiento
    }
}

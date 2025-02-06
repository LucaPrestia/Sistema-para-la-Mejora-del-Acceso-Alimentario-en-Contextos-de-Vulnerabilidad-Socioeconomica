package ar.utn.sistema.entities.incidente;

import ar.utn.sistema.entities.PersistenciaID;
import ar.utn.sistema.entities.heladera.EstadoHeladera;
import ar.utn.sistema.entities.usuarios.Tecnico;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Entity
@Getter @Setter @NoArgsConstructor
public class VisitaIncidente extends PersistenciaID {
    // un incidente puede tener varias visitas de distintos técnicos y a su vez un técnico puede visitar distintos incidentes
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})  // un incidente puede tener muchas visitas
    @JoinColumn(name = "id_incidente", referencedColumnName = "id")
    private Incidente incidente;

    @ManyToOne(fetch = FetchType.LAZY) // un incidente puede tener muchos tecnicos (cada visita la puede realizar un tecnico diferente)
    @JoinColumn(name = "tecnicoId", referencedColumnName = "id")
    private Tecnico tecnico;

    private LocalDate fechaVisita;
    private String descripcionTrabajo;
    @Lob // Define que este campo almacenará datos binarios grandes
    @Column(columnDefinition = "VARBINARY(MAX)") // Compatible con SQL Server
    private byte[] foto;
    private boolean solucionado = false;

    public VisitaIncidente(Incidente incidente, Tecnico tecnico, String descripcionTrabajo, byte[] foto, boolean solucionado) {
        this.incidente = incidente;
        this.tecnico = tecnico;
        this.descripcionTrabajo = descripcionTrabajo;
        this.foto = foto;
        this.fechaVisita = LocalDate.now();
        this.solucionado = solucionado;
        if (solucionado){
            incidente.setEstado("RESUELTO");
            incidente.getHeladera().setEstado(EstadoHeladera.ACTIVA); // si se soluciono la heladera vuelve a estar en funcionamiento
        }
    }
}

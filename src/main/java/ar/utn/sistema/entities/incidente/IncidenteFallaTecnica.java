package ar.utn.sistema.entities.incidente;

import ar.utn.sistema.entities.heladera.Heladera;
import ar.utn.sistema.entities.usuarios.Colaborador;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Entity
@DiscriminatorValue("fallaTecnica")
@Getter @Setter @NoArgsConstructor
public class IncidenteFallaTecnica extends Incidente{
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_colaborador_notificador")
    private Colaborador notificador;
    private String descripcion;
    @Lob // Define que este campo almacenará datos binarios grandes
    @Column(columnDefinition = "VARBINARY(MAX)") // Compatible con SQL Server
    private byte[] foto;

    public IncidenteFallaTecnica(LocalDateTime fechaHora, Heladera heladera, Colaborador notificador, String descripcion, byte[] foto,String zona) {
        super(fechaHora, heladera, "falla_tecnica");
        this.notificador = notificador;
        this.descripcion = descripcion;
        this.foto = foto;
        this.setZona(zona);
    }
}

package ar.utn.sistema.entities.incidente;

import ar.utn.sistema.entities.heladera.Heladera;
import ar.utn.sistema.entities.usuarios.Colaborador;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Entity
@DiscriminatorValue("fallaTecnica")
@Getter @Setter @NoArgsConstructor
public class IncidenteFallaTecnica extends Incidente{
    private Colaborador notificador;
    private String descripcion;
    private byte[] foto;

    public IncidenteFallaTecnica(LocalDateTime fechaHora, Heladera heladera, Colaborador notificador, String descripcion, byte[] foto) {
        super(fechaHora, heladera, "falla_tecnica");
        this.notificador = notificador;
        this.descripcion = descripcion;
        this.foto = foto;
    }
}

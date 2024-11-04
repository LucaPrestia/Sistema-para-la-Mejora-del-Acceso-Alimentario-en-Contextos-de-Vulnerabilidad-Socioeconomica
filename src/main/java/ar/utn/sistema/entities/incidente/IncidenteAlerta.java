package ar.utn.sistema.entities.incidente;

import ar.utn.sistema.entities.heladera.Heladera;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor
@Entity
@DiscriminatorValue("alerta")

public class IncidenteAlerta extends Incidente{
    @Enumerated(EnumType.STRING)
    private TipoAlerta tipoAlerta;

    public IncidenteAlerta(LocalDateTime fechaHora, Heladera heladera, TipoAlerta tipoAlerta) {
        super(fechaHora, heladera, "alerta");
        this.tipoAlerta = tipoAlerta;
    }
}

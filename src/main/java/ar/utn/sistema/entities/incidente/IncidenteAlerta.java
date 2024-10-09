package ar.utn.sistema.entities.incidente;

import ar.utn.sistema.entities.heladera.Heladera;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor
public class IncidenteAlerta extends Incidente{
    private String tipoAlerta; // ("FRAUDE", "TEMPERATURA", "CONEXION")

    public IncidenteAlerta(LocalDateTime fechaHora, Heladera heladera, String tipoAlerta) {
        super(fechaHora, heladera, "alerta");
        this.tipoAlerta = tipoAlerta;
    }
}

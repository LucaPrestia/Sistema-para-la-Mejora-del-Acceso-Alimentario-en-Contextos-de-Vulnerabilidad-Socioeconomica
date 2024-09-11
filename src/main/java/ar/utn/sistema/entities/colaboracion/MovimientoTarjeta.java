package ar.utn.sistema.entities.colaboracion;

import ar.utn.sistema.entities.heladera.Heladera;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
@Getter@Setter@NoArgsConstructor
public class MovimientoTarjeta {
    private Heladera heladera;
    private Date fechaUso = new Date();

    public MovimientoTarjeta(Heladera heladera) {
        this.heladera = heladera;
    }
}

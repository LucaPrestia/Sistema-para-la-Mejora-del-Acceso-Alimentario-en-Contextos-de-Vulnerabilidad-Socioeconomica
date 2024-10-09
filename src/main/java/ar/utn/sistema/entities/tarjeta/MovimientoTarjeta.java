package ar.utn.sistema.entities.tarjeta;

import ar.utn.sistema.entities.PersistenciaID;
import ar.utn.sistema.entities.heladera.Heladera;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter @Setter @NoArgsConstructor
public class MovimientoTarjeta extends PersistenciaID {
    private Heladera heladera;
    private LocalDateTime fechaApertura;
    private LocalDateTime fechaPedidoMovimiento;
    private MotivoMovimientoTarjeta motivo;
    private int cantidadViandas;

    public MovimientoTarjeta(Heladera heladera, MotivoMovimientoTarjeta motivo, int cantidadViandas) {
        this.heladera = heladera;
        this.motivo = motivo;
    }
}

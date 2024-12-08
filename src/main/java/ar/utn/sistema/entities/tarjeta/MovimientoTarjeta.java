package ar.utn.sistema.entities.tarjeta;

import ar.utn.sistema.entities.PersistenciaID;
import ar.utn.sistema.entities.heladera.Heladera;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Entity
@Getter @Setter @NoArgsConstructor
public class MovimientoTarjeta extends PersistenciaID {
    @OneToOne
    private Heladera heladera;
    private LocalDateTime fechaApertura;
    private LocalDateTime fechaPedidoMovimiento;
    @Enumerated(EnumType.STRING)
    private MotivoMovimientoTarjeta motivo;
    private int cantidadViandas;

    public MovimientoTarjeta(Heladera heladera, MotivoMovimientoTarjeta motivo, int cantidadViandas) {
        this.heladera = heladera;
        this.motivo = motivo;
    }
}

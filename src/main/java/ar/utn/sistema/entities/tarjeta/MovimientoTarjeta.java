package ar.utn.sistema.entities.tarjeta;

import ar.utn.sistema.entities.PersistenciaID;
import ar.utn.sistema.entities.heladera.Heladera;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Entity
@Getter @Setter @NoArgsConstructor
public class MovimientoTarjeta extends PersistenciaID {

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "id_heladera", referencedColumnName = "id")
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

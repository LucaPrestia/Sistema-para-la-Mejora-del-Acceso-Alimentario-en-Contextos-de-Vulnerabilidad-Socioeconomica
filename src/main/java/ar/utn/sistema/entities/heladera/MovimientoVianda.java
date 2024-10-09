package ar.utn.sistema.entities.heladera;

import ar.utn.sistema.entities.PersistenciaID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor
public class MovimientoVianda extends PersistenciaID {
    private LocalDateTime fechaHoraMovimiento;
    // @Enumerated(EnumType.STRING)
    private TipoMovimientoVianda tipoMovimiento;
    private String motivoMovimiento; // si aplica (para redistribución)
    // @ManyToOne
    private Heladera origenHeladera;
    // @ManyToOne
    private Heladera destinoHeladera; // si aplica (para redistribución)

    public MovimientoVianda(TipoMovimientoVianda tipoMovimiento, Heladera origenHeladera, Heladera destinoHeladera, String motivo) {
        this.fechaHoraMovimiento = LocalDateTime.now();
        this.tipoMovimiento = tipoMovimiento;
        this.origenHeladera = origenHeladera;
        this.destinoHeladera = destinoHeladera;
        this.motivoMovimiento = motivo;
    }
}

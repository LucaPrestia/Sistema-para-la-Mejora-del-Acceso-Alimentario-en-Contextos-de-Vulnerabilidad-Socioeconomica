package ar.utn.sistema.entities.heladera;

import ar.utn.sistema.entities.PersistenciaID;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Entity
@Getter @Setter @NoArgsConstructor
public class MovimientoVianda extends PersistenciaID {
    private LocalDateTime fechaHoraMovimiento;
    @Enumerated(EnumType.STRING)
    private TipoMovimientoVianda tipoMovimiento;
    private String motivoMovimiento; // si aplica (para redistribución)
    @ManyToOne
    @JoinColumn(name = "heladeraOrigen", referencedColumnName = "id")
    private Heladera origenHeladera;
    @ManyToOne
    @JoinColumn(name = "heladeraDestino", referencedColumnName = "id")
    private Heladera destinoHeladera; // si aplica (para redistribución)

    public MovimientoVianda(TipoMovimientoVianda tipoMovimiento, Heladera origenHeladera, Heladera destinoHeladera, String motivo) {
        this.fechaHoraMovimiento = LocalDateTime.now();
        this.tipoMovimiento = tipoMovimiento;
        this.origenHeladera = origenHeladera;
        this.destinoHeladera = destinoHeladera;
        this.motivoMovimiento = motivo;
    }
}

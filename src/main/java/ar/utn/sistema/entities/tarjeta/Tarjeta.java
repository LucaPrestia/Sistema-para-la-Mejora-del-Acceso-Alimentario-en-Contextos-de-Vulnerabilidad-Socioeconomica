package ar.utn.sistema.entities.tarjeta;

import ar.utn.sistema.entities.PersistenciaID;
import ar.utn.sistema.entities.heladera.Heladera;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Entity
@Inheritance(strategy=InheritanceType.JOINED)
@Getter @Setter @NoArgsConstructor
public abstract class Tarjeta extends PersistenciaID {
    private String codigo;
    private LocalDate fechaActivada;
    @OneToMany
    private List<MovimientoTarjeta> movimientos;

    public Tarjeta(String codigo) {
        this.codigo = codigo;
        this.fechaActivada = LocalDate.now();
        this.movimientos = new ArrayList<MovimientoTarjeta>();
    }

    public void agregarMovimiento(MovimientoTarjeta movimiento){
        this.movimientos.add(movimiento);
    }

    protected void autorizarAperturaHeladera(Heladera heladera){
        // todo: enviar autorización apertura de esta tarjeta a la heladera
    }

    public abstract boolean usarTarjeta(Heladera heladera);
}

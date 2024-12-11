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
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_usuario") // Obligatorio para asignarle un nombre al campo que va a determinar de qué tipo de clase hija está conteniendo los datos
@Table(name = "tarjeta")
@Getter @Setter @NoArgsConstructor
public abstract class Tarjeta extends PersistenciaID {

    private String codigo;
    private LocalDate fechaActivada;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
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

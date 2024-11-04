package ar.utn.sistema.entities.heladera;

import ar.utn.sistema.entities.PersistenciaID;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@Entity
@Getter @Setter @NoArgsConstructor
public class Vianda extends PersistenciaID {

    private String comida;
    private LocalDate fechaCaducidad;
    // private Heladera heladera; // hace falta?? ya se persiste en los movimientos de las viandas y cuando está en una heladera se encuentra dentro de la lista de heladeras. lo comento por el momento para evitar bidireccionalidad
    private Float calorias;
    private Float peso;
    @OneToMany
    @JoinColumn(name = "viandaId",referencedColumnName = "id")
    private List<MovimientoVianda> movimientos = new ArrayList<MovimientoVianda>();

    public Vianda(String comida, LocalDate fechaCaducidad, Heladera heladera, Float calorias, Float peso) {
        this.comida = comida;
        this.fechaCaducidad = fechaCaducidad;
        this.calorias = calorias;
        this.peso = peso;

        MovimientoVianda movimiento = new MovimientoVianda(TipoMovimientoVianda.DONACION, heladera, null, null);
        agregarMovimientoVianda(movimiento);
        heladera.agregarVianda(this);
    }
    public void consumirVianda(Heladera heladera) {
        MovimientoVianda movimiento = new MovimientoVianda(TipoMovimientoVianda.CONSUMO, heladera, null, null);
        agregarMovimientoVianda(movimiento);
        heladera.sacarVianda(this);
    }

    public void agregarMovimientoVianda(MovimientoVianda movimiento) {
        this.movimientos.add(movimiento);
    }
}

package ar.utn.sistema.entities.colaboracion;

import ar.utn.sistema.entities.heladera.Heladera;
import ar.utn.sistema.entities.heladera.MovimientoVianda;
import ar.utn.sistema.entities.heladera.TipoMovimientoVianda;
import ar.utn.sistema.entities.heladera.Vianda;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@Entity
@Getter @Setter @NoArgsConstructor
public class ColaboracionDistribucionViandas extends Colaboracion {
    @OneToOne
    private Heladera origenHeladera;
    @OneToOne
    private Heladera destinoHeladera;
    // @Transient --> esto se agrega para que no persista este artributo en la base de datos
    @OneToMany
    private List<Vianda> viandas = new ArrayList<Vianda>();
    private int cantidad;
    private String motivoDistribucion;

    public ColaboracionDistribucionViandas(Heladera orgHeladera, Heladera destHeladera, List<Vianda> viandas, String motivo){
        super(TipoColaboracionEnum.REDISTRIBUCION_VIANDAS, 1.0);
        // TODO: cuando agreguemos la base de datos, vamos a tomar el coeficiente desde la tabla CoeficientesColaboracion
        this.origenHeladera = orgHeladera;
        this.destinoHeladera = destHeladera;
        this.viandas = viandas;
        this.cantidad = viandas.size();
        this.motivoDistribucion = motivo;

        for (Vianda v: viandas){
            MovimientoVianda movimiento = new MovimientoVianda(TipoMovimientoVianda.REDISTRIBUCION,orgHeladera, destHeladera, motivo);
            v.agregarMovimientoVianda(movimiento);
            // luego de registrar estos movimientos se genera el pedidoMovimientoTarjeta Colaborador (EGRESO e INGRESO VIANDA DISTRIBUCION) indicando la cantidad de viandas en total que se deban agregar o sacar en cada caso
            // las viandas luego se sacan de la heladera origen (el sistema le permitirá sacar sólo la cantidad indicada en el pedido de tarjeta)
            // las viandas luego se agregan de la heladera destino (el sistema le permitirá agregar sólo la cantidad indicada en el pedido de tarjeta)
        }

    }

    public ColaboracionDistribucionViandas(int cantidadCargaMasiva, LocalDate fechaCargaMasiva) {
        super(TipoColaboracionEnum.REDISTRIBUCION_VIANDAS, 1.0);
        this.cantidad = cantidadCargaMasiva;
        this.setFechaColaboracion(fechaCargaMasiva);
        /* en teoría, como no hay datos de las viandas, no se crean en la base
        for(int i=0;i<cantidad;i++){
            this.viandas.add(new Vianda());
        }
        */
    }

    public void realizarDistribucion(){
        // TODO
    }

    @Override
    public double sumarPuntos() {
        return this.cantidad * this.getCoeficientePuntos();
    }
}

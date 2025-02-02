package ar.utn.sistema.entities.colaboracion;

import ar.utn.sistema.entities.heladera.Heladera;
import ar.utn.sistema.entities.heladera.MovimientoVianda;
import ar.utn.sistema.entities.heladera.TipoMovimientoVianda;
import ar.utn.sistema.entities.heladera.Vianda;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@Entity
@Getter @Setter @NoArgsConstructor
@Table(name = "colaboracion_distribucion_viandas")
public class ColaboracionDistribucionViandas extends Colaboracion {

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "id_origen_heladera")
    private Heladera origenHeladera;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "id_destino_heladera")
    private Heladera destinoHeladera;
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
   // @JoinColumn(name = "id_vianda")
    private List<Vianda> viandas = new ArrayList<Vianda>();

    private int cantidad;
    private String motivoDistribucion;

    public ColaboracionDistribucionViandas(Heladera orgHeladera, Heladera destHeladera, List<Vianda> viandas, String motivo, Double coeficiente){
        super(TipoColaboracionEnum.REDISTRIBUCION_VIANDAS, coeficiente);
        this.origenHeladera = orgHeladera;
        this.destinoHeladera = destHeladera;
        this.viandas = viandas;
        this.cantidad = viandas.size();
        this.motivoDistribucion = motivo;

        for (Vianda v: viandas){
            MovimientoVianda movimiento = new MovimientoVianda(TipoMovimientoVianda.REDISTRIBUCION,orgHeladera, destHeladera, motivo);
            v.agregarMovimientoVianda(movimiento);
            destHeladera.agregarVianda(v);
            orgHeladera.sacarVianda(v);

            // luego de registrar estos movimientos se genera el pedidoMovimientoTarjeta Colaborador (EGRESO e INGRESO VIANDA DISTRIBUCION) indicando la cantidad de viandas en total que se deban agregar o sacar en cada caso
            // las viandas luego se sacan de la heladera origen (el sistema le permitirá sacar sólo la cantidad indicada en el pedido de tarjeta)
            // las viandas luego se agregan de la heladera destino (el sistema le permitirá agregar sólo la cantidad indicada en el pedido de tarjeta)
        }

    }

    public ColaboracionDistribucionViandas(int cantidadCargaMasiva, LocalDate fechaCargaMasiva, Double coeficienteCargaMasiva) {
        super(TipoColaboracionEnum.REDISTRIBUCION_VIANDAS, coeficienteCargaMasiva);
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

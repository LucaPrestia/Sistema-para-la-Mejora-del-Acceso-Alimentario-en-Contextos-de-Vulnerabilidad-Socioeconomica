package ar.utn.sistema.entities.colaboracion;

import ar.utn.sistema.entities.heladera.Vianda;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
@Entity
@Getter @Setter @NoArgsConstructor
@Table(name = "colaboracion_vianda")
public class ColaboracionVianda extends Colaboracion {
    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
    @JoinColumn(name = "id_colaboracion_vianda")
    private List<Vianda> vianda; // en el NEW de cada vianda ya se cargó un movimiento de vianda de tipo donacion
    private int cantidad;

    public ColaboracionVianda(List<Vianda> vianda, int cantidad, Double coeficiente){
        super(TipoColaboracionEnum.DONACION_VIANDAS, coeficiente);
        this.vianda = vianda;
        this.cantidad = cantidad;
    }

    // para la carga masiva: no importa qué viandas se distribuyeron sino la cantidad para sumar puntos!
    public ColaboracionVianda(int cantidadCargaMasiva, LocalDate fechaCargaMasiva, Double coeficienteCargaMasiva){
        super(TipoColaboracionEnum.DONACION_VIANDAS, coeficienteCargaMasiva);
        this.cantidad = cantidadCargaMasiva;
        this.setFechaColaboracion(fechaCargaMasiva);
    }

    @Override
    public double sumarPuntos(){
        return this.getCoeficientePuntos() * this.cantidad;
    }

}

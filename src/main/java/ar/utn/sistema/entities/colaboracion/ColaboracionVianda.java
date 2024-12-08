package ar.utn.sistema.entities.colaboracion;

import ar.utn.sistema.entities.heladera.Vianda;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
@Entity
@Getter @Setter @NoArgsConstructor
public class ColaboracionVianda extends Colaboracion {
    @OneToMany
    private List<Vianda> vianda; // en el NEW de cada vianda ya se cargó un movimiento de vianda de tipo donacion
    private int cantidad;

    public ColaboracionVianda(List<Vianda> vianda, int cantidad){
        super(TipoColaboracionEnum.DONACION_VIANDAS, 1.5);
        // TODO: cuando agreguemos la base de datos, vamos a tomar el coeficiente desde la tabla CoeficientesColaboracion
        this.vianda = vianda;
        this.cantidad = cantidad;
    }

    // para la carga masiva: no importa qué viandas se distribuyeron sino la cantidad para sumar puntos!
    public ColaboracionVianda(int cantidadCargaMasiva, LocalDate fechaCargaMasiva){
        super(TipoColaboracionEnum.DONACION_VIANDAS, 1.5);
        this.cantidad = cantidadCargaMasiva;
        this.setFechaColaboracion(fechaCargaMasiva);
    }

    @Override
    public double sumarPuntos(){
        return this.getCoeficientePuntos() * this.cantidad;
    }

}

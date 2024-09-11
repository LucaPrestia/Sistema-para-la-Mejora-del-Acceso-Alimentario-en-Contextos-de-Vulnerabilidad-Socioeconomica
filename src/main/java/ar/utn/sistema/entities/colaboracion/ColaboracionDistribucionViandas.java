package ar.utn.sistema.entities.colaboracion;

import ar.utn.sistema.entities.heladera.Heladera;
import ar.utn.sistema.entities.heladera.Vianda;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter @NoArgsConstructor
public class ColaboracionDistribucionViandas extends Colaboracion {
    private Heladera origenHeladera;
    private Heladera destinoHeladera;
    private List<Vianda> viandas = new ArrayList<Vianda>();
    private String motivoDistribucion; // TODO: campo libre, enum o tabla en base?

    public ColaboracionDistribucionViandas(Heladera orgHeladera, Heladera destHeladera, List<Vianda> viandas, String motivo){
        super(TipoColaboracion.DISTRUBUCION_VIANDA, 1.0);
        // TODO: cuando agreguemos la base de datos, vamos a tomar el coeficiente desde la tabla CoeficientesColaboracion
        this.origenHeladera = orgHeladera;
        this.destinoHeladera = destHeladera;
        this.viandas = viandas;
        this.motivoDistribucion = motivo;
    }

    public ColaboracionDistribucionViandas(int cantidad) {
        for(int i=0;i<cantidad;i++){
            this.viandas.add(new Vianda());
        }
    }

    public void realizarDistribucion(){
        // TODO
    }

    @Override
    public double sumarPuntos() {
        return this.viandas.size() * this.getCoeficientePuntos();
    }
}

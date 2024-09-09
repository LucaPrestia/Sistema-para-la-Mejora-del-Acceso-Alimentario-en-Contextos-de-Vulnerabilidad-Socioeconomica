package ar.utn.sistema.entities.colaboracion;

import ar.utn.sistema.entities.heladera.Vianda;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class ColaboracionVianda extends Colaboracion {
    private Vianda vianda;
    // private int cantidad;  TODO: evaluar si se pueden donar de a varias viandas del mismo tipo de alimento

    public ColaboracionVianda(Vianda vianda){
        super(TipoColaboracion.DONACION_VIANDA, 1.5);
        // TODO: cuando agreguemos la base de datos, vamos a tomar el coeficiente desde la tabla CoeficientesColaboracion
        this.vianda = vianda;
    }

    public void cargarVianda(){
     //   TODO
    }

}

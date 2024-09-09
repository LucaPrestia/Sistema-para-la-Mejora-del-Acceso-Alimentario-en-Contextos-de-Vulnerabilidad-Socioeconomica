package ar.utn.sistema.entities.colaboracion;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class ColaboracionDinero extends Colaboracion {
    private Float monto;
    private TipoFrecuencia frencuencia;

    public ColaboracionDinero(Float monto, TipoFrecuencia frecuencia){
        super(TipoColaboracion.DONACION_DINERO, 0.5);
        // TODO: cuando agreguemos la base de datos, vamos a tomar el coeficiente desde la tabla CoeficientesColaboracion
        this.monto = monto;
        this.frencuencia = frecuencia;
    }

    public void donarDinero(){
        // TODO
    }

    @Override
    public double sumarPuntos() {
        return this.frencuencia == TipoFrecuencia.UNICA ? this.monto * this.getCoeficientePuntos() : 0;
    }
}

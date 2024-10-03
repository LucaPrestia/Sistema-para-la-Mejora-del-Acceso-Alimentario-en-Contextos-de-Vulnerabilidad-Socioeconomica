package ar.utn.sistema.entities.colaboracion;

import ar.utn.sistema.entities.heladera.Heladera;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class ColaboracionGestionHeladera extends Colaboracion{
    private Heladera heladera;

    public ColaboracionGestionHeladera(Heladera heladera){
        super(TipoColaboracionEnum.GESTION_HELADERA, 5.0);
        this.heladera = heladera;
    }

    @Override
    public double sumarPuntos() {
        return this.heladera.mesesActiva() * this.getCoeficientePuntos();
    }
}

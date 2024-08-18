package ar.utn.sistema.entities.colaboracion;

import ar.utn.sistema.entities.heladera.Heladera;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class ColaboracionDistribucion {
    private Heladera origenHeladera;
    private Heladera destinoHeladera;
    private Integer cantidadViandas;
    private String motivoDistribucion; // TODO: campo libre, enum o tabla en base?
}

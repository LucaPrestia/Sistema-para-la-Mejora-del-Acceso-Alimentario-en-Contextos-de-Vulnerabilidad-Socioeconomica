package entities.colaboracion;

import entities.heladera.Heladera;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
@Entity
@DiscriminatorValue(value = "distribucion")
public class ColaboracionDistribucion {
    @ManyToOne
    @JoinColumn(name = "heladera_origen")
    private Heladera origenHeladera;

    @ManyToOne
    @JoinColumn(name = "heladera_destino")
    private Heladera destinoHeladera;

    @Column(name = "cantidad_viandas")
    private Integer cantidadViandas;
    @Column(name = "motivo_distribucion")
    private String motivoDistribucion; // TODO: campo libre, enum o tabla en base?
}

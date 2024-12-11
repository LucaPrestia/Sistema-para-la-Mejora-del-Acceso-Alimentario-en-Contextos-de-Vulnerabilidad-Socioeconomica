package ar.utn.sistema.entities.colaboracion;

import ar.utn.sistema.entities.heladera.Heladera;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@Getter @Setter @NoArgsConstructor
@Table(name = "colaboracion_gestion_heladera")
public class ColaboracionGestionHeladera extends Colaboracion{
    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
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

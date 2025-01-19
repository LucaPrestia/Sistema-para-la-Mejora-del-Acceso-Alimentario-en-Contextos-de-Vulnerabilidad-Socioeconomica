package ar.utn.sistema.entities.configuracion;

import ar.utn.sistema.entities.PersistenciaID;
import ar.utn.sistema.entities.colaboracion.TipoColaboracionEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CoeficientesColaboracion extends PersistenciaID {
    @OneToOne(fetch = FetchType.LAZY)
    private TipoColaboracion tipoColaboracion;

    private Double coeficientePuntos;
}

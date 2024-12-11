package ar.utn.sistema.entities.configuracion;

import ar.utn.sistema.entities.PersistenciaID;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ParametrosGenerales extends PersistenciaID {
    private String parametro;
    private BigDecimal valor;
}

package ar.utn.sistema.entities.colaboracion;

import ar.utn.sistema.entities.PersistenciaID;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@Getter @Setter @NoArgsConstructor
public class TipoColaboracion extends PersistenciaID {
    private Integer id;
    private String codigo;
    private String nombre;

    public TipoColaboracion(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }
}

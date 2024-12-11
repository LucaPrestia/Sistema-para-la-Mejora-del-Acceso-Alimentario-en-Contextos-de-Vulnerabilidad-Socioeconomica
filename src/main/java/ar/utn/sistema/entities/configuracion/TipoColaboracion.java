package ar.utn.sistema.entities.configuracion;

import ar.utn.sistema.entities.PersistenciaID;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class TipoColaboracion extends PersistenciaID {
    private String codigo;
    private String nombre;

    public TipoColaboracion(String nombre) {
        this.nombre = nombre;
    }
}

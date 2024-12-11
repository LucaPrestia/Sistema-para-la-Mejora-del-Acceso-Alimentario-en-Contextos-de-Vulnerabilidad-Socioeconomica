package ar.utn.sistema.entities.configuracion;

import ar.utn.sistema.entities.PersistenciaID;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter @NoArgsConstructor
public class ColaboradorColaboracion extends PersistenciaID {
    private String tipoColaborador;

    @ManyToMany
    @JoinTable(
            name = "colaborador_colaboracion_tipo_colaboracion", // Nombre de la tabla intermedia
            joinColumns = @JoinColumn(name = "id_colaborador_colaboracion"), // Clave foránea en la tabla intermedia
            inverseJoinColumns = @JoinColumn(name = "id_tipo_colaboracion") // Clave foránea en la tabla intermedia
    )
    private List<TipoColaboracion> tipoColaboracion = new ArrayList<TipoColaboracion>();
    // en este caso esta bien que cree una tabla intermedia!!

    public ColaboradorColaboracion(String tipoColaborador, List<TipoColaboracion> tipoColaboracion) {
        this.tipoColaborador = tipoColaborador;
        this.tipoColaboracion = tipoColaboracion;
    }

    public void agregarTipoColaboracion(TipoColaboracion tipoColaboracion) {
        this.tipoColaboracion.add(tipoColaboracion);
    }
}

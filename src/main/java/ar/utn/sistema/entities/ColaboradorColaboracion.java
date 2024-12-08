package ar.utn.sistema.entities;

import ar.utn.sistema.entities.colaboracion.TipoColaboracion;
import ar.utn.sistema.entities.usuarios.Colaborador;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter @NoArgsConstructor
public class ColaboradorColaboracion extends PersistenciaID {
    private String tipoColaborador;
    @OneToMany
    private List<TipoColaboracion> tipoColaboracion = new ArrayList<TipoColaboracion>();

    public ColaboradorColaboracion(String tipoColaborador, List<TipoColaboracion> tipoColaboracion) {
        this.tipoColaborador = tipoColaborador;
        this.tipoColaboracion = tipoColaboracion;
    }

    public void agregarTipoColaboracion(TipoColaboracion tipoColaboracion) {
        this.tipoColaboracion.add(tipoColaboracion);
    }
}

package ar.utn.sistema.entities;

import ar.utn.sistema.entities.colaboracion.TipoColaboracion;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter @NoArgsConstructor
public class ColaboradorColaboracion {
    private String tipoColaborador;
    private List<TipoColaboracion> tipoColaboracion = new ArrayList<TipoColaboracion>();

    public ColaboradorColaboracion(String tipoColaborador, List<TipoColaboracion> tipoColaboracion) {
        this.tipoColaborador = tipoColaborador;
        this.tipoColaboracion = tipoColaboracion;
    }

    public void agregarTipoColaboracion(TipoColaboracion tipoColaboracion) {
        this.tipoColaboracion.add(tipoColaboracion);
    }
}

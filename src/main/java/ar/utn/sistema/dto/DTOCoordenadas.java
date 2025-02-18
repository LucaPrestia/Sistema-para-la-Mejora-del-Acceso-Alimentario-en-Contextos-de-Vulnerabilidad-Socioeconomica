package ar.utn.sistema.dto;

import ar.utn.sistema.entities.PersistenciaID;
import ar.utn.sistema.entities.heladera.Heladera;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter
@Entity
public class DTOCoordenadas extends PersistenciaID {
    private double lat;
    private double lon;
    private String nombre;
    private String direccion;
    private String estado;
    private int stockViandas;
    private int espacioDisponible;
    public DTOCoordenadas(double v, double v1) {
        lat = v;
        lon = v1;
    }

    public DTOCoordenadas() {

    }
}

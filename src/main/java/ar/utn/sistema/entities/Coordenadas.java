package ar.utn.sistema.entities;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter
@Entity
public class Coordenadas extends PersistenciaID {
    private double latitud;
    private double longitud;

    public Coordenadas(double v, double v1) {
        latitud = v;
        longitud = v1;
    }

    public Coordenadas() {

    }
}

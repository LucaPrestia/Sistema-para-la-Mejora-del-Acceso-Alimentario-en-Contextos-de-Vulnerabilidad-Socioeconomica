package ar.utn.sistema.entities;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class Coordenadas {
    private double latitud;
    private double longitud;

    public Coordenadas(double v, double v1) {
        latitud = v;
        longitud = v1;
    }
}

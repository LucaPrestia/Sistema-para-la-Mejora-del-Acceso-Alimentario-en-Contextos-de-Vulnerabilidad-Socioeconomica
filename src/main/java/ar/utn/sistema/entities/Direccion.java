package ar.utn.sistema.entities;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class Direccion {
    private double lat;
    private double lon;
    // Pair<Integer,Integer> pair = new Pair<>(lat, lon);// a checkear, lo comente porque no me estaba tomando el Pair
    private String pais; // luego hacer un ENUM
    private String provincia;
    private String localidad;
    private String calle;
    private int numero;
}

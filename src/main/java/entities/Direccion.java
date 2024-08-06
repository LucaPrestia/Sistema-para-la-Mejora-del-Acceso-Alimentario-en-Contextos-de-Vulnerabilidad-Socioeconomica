package entities;

import jakarta.persistence.Embeddable;
import jdk.internal.net.http.common.Pair;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Embeddable
public class Direccion {
    private int coordenada1;
    private int coordenada2;
    Pair<Integer,Integer> pair = new Pair<>(coordenada1, coordenada2);// a checkear
    private String pais; // luego hacer un ENUM
    private String provincia;
    private String localidad;
    private String calle;
    private int numero;
}

package ar.utn.sistema.entities;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class Direccion {
    private double lat;
    private double lon;
    private String pais;
    private String provincia;
    private String localidad;
    private String calle;
    private int numero;
    private int codigo_postal;
}

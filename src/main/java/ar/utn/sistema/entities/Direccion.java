package ar.utn.sistema.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
public class Direccion {
    private double lat;
    private double lon;
    private String pais;
    private String provincia;
    private String localidad;
    private String calle;
    private int numero;
    private String departamento;
    private int codigo_postal;

    public Direccion(String pais, String provincia, String localidad, String calle, int numero, int codigo_postal, String departamento) {
        this.pais = pais;
        this.provincia = provincia;
        this.localidad = localidad;
        this.calle = calle;
        this.numero = numero;
        this.codigo_postal = codigo_postal;
        this.departamento = departamento;
    }

    public Direccion(double lat, double lon, String pais, String provincia, String localidad, String calle, int numero, int codigo_postal, String departamento) {
        this.lat = lat;
        this.lon = lon;
        this.pais = pais;
        this.provincia = provincia;
        this.localidad = localidad;
        this.calle = calle;
        this.numero = numero;
        this.codigo_postal = codigo_postal;
        this.departamento = departamento;
    }

    public String obtenerCadenaDireccion() {
        StringBuilder direccion = new StringBuilder();
        direccion.append(calle).append(" ").append(numero);
        if (departamento != null && !departamento.isEmpty()) {
            direccion.append(" (Dto. ").append(departamento).append(")");
        }
        direccion.append(" - ").append(codigo_postal)
                .append(" ").append(localidad)
                .append(", ").append(provincia)
                .append(" ").append(pais.toUpperCase());

        return direccion.toString();
    }


}

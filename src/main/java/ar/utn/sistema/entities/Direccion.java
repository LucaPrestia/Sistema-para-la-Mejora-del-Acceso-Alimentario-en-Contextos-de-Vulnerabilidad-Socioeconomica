package ar.utn.sistema.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Direccion extends PersistenciaID {

    @OneToOne(cascade = CascadeType.ALL)
    private Coordenadas coordenadas;

    private String pais;
    private String provincia;
    private String localidad;
    private String calle;
    private Integer numero;
    private String departamento;
    private Integer codigo_postal;

    public Direccion(Coordenadas coordenadas, String pais, String provincia, String localidad, String calle, int numero, int codigo_postal, String departamento) {
        this.coordenadas = coordenadas;
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

        direccion.append(calle != null ? calle : "").append(" ").append(numero);

        if (departamento != null && !departamento.isEmpty()) {
            direccion.append(" (Dto. ").append(departamento).append(")");
        }

        direccion.append(" - ").append(codigo_postal);

        if (localidad != null && !localidad.isEmpty()) {
            direccion.append(" ").append(localidad);
        }
        if (provincia != null && !provincia.isEmpty()) {
            direccion.append(", ").append(provincia);
        }
        if (pais != null && !pais.isEmpty()) {
            direccion.append(" ").append(pais.toUpperCase());
        }

        return direccion.toString().trim();
    }
}

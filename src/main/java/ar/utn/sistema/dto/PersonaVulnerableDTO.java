package ar.utn.sistema.dto;


import ar.utn.sistema.entities.usuarios.TipoDocumento;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Setter
@Getter
@NoArgsConstructor
public class PersonaVulnerableDTO {
    private String nombre;
    private String fechaNacimiento;
    private String fechaRegistro;
    private Boolean situacionDeCalle;
    private String documento;
    private Integer menoresACargo;
    private String pais;
    private String provincia;
    private String localidad;
    private String calle;
    private Integer numero;
    private String departamento;
    private Integer codigoPostal;

}



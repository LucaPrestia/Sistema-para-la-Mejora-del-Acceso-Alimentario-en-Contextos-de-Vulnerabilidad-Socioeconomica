package ar.utn.sistema.entities.usuarios;

import ar.utn.sistema.entities.Direccion;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Entity
@Getter @Setter @NoArgsConstructor
public class PersonaVulnerable extends Rol {
    private String nombre;
    private LocalDate fechaNacimiento;
    private LocalDate fechaRegistro;
    private Boolean situacionDeCalle;
    @Embedded
    private Direccion direccion;
    private TipoDocumento tipoDocumento;
    private String documento;
    private Integer menoresACargo;
}

package ar.utn.sistema.entities.usuarios;

import ar.utn.sistema.entities.Direccion;
import ar.utn.sistema.entities.PersistenciaID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor
public class PersonaVulnerable extends PersistenciaID {
    private Usuario usuario;
    private String nombre;
    private LocalDate fechaNacimiento;
    private LocalDate fechaRegistro;
    private Boolean situacionDeCalle;
    private Direccion direccion;
    private TipoDocumento tipoDocumento;
    private String documento;
    private Integer menoresACargo;
}

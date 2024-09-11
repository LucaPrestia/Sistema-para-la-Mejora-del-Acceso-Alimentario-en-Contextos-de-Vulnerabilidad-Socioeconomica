package ar.utn.sistema.entities.usuarios;

import ar.utn.sistema.entities.Direccion;
import ar.utn.sistema.entities.PersistenciaID;
import ar.utn.sistema.entities.colaboracion.Tarjeta;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.swing.table.TableRowSorter;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor
public class PersonaVulnerable extends Rol {
    private String nombre;
    private LocalDate fechaNacimiento;
    private LocalDate fechaRegistro;
    private Boolean situacionDeCalle;
    private Direccion direccion;
    private TipoDocumento tipoDocumento;
    private String documento;
    private Integer menoresACargo;


}

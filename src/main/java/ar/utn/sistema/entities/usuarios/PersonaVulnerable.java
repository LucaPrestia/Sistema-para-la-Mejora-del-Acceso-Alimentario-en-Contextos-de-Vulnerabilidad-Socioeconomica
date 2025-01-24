package ar.utn.sistema.entities.usuarios;

import ar.utn.sistema.entities.Direccion;
import ar.utn.sistema.entities.tarjeta.TarjetaPersonaVulnerable;
import jakarta.persistence.*;
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

    @OneToOne
    private Direccion direccion;

    @Enumerated(EnumType.STRING)
    private TipoDocumento tipoDocumento;

    private String documento;
    private Integer menoresACargo;

    @OneToOne(fetch = FetchType.LAZY)
    private TarjetaPersonaVulnerable tarjeta;
}

package ar.utn.sistema.entities.usuarios;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Entity
@Getter @Setter @NoArgsConstructor
public class ColaboradorFisico extends Colaborador{
    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento;
    @Enumerated(EnumType.STRING)
    private TipoDocumento tipoDocumento;
    private String documento;

    public ColaboradorFisico(String nombre, String apellido, LocalDate fechaNacimiento,
                             TipoDocumento tipoDocumento, String documento) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.fechaNacimiento = fechaNacimiento;
        this.tipoDocumento = tipoDocumento;
        this.documento = documento;
    }

    public ColaboradorFisico(String nombre, String apellido, TipoDocumento tipoDocumento, String documento) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.tipoDocumento = tipoDocumento;
        this.documento = documento;
    }
}

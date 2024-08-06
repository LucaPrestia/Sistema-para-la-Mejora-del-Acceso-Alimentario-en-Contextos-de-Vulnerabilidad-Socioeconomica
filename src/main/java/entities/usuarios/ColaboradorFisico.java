package entities.usuarios;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "colaborador_fisico")
@Getter @Setter @NoArgsConstructor
public class ColaboradorFisico extends Colaborador{
    private String nombre;
    private String apellido;
    @Column(name = "fecha_nacimiento")
    private Date nacimiento;
}

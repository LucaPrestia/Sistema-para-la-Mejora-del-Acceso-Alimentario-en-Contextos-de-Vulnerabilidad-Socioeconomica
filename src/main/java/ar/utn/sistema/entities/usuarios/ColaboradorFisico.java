package ar.utn.sistema.entities.usuarios;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter @Setter @NoArgsConstructor
public class ColaboradorFisico extends Colaborador{
    private String nombre;
    private String apellido;
    private Date nacimiento;
}

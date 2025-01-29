package ar.utn.sistema.dto;

import ar.utn.sistema.entities.Direccion;
import ar.utn.sistema.entities.notificacion.Contacto;
import ar.utn.sistema.entities.usuarios.TipoDocumento;
import ar.utn.sistema.entities.usuarios.TipoJuridico;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter @Setter
public class PerfilColaboradorDto {
    // datos basico persona humana
    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento;
    private TipoDocumento tipoDocumento;
    private String documento;

    // datos basicos persona juridica
    private String razonSocial;
    private String rubro;
    private String cuit;
    private TipoJuridico tipoJuridico;

    private List<Contacto> contactos;
    private Direccion direccion;
}

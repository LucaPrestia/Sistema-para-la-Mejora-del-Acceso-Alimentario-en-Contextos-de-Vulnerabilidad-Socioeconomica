package ar.utn.sistema.dto;

import ar.utn.sistema.entities.Direccion;
import ar.utn.sistema.entities.notificacion.Contacto;
import ar.utn.sistema.entities.usuarios.TipoDocumento;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class TecnicoAltaDto {
    private String username;
    private String nombre;
    private String apellido;
    private Long cuil;
    private TipoDocumento tipoDocumento;
    private Long documento;
    private List<Contacto> contactos;
    private Direccion direccion;
}

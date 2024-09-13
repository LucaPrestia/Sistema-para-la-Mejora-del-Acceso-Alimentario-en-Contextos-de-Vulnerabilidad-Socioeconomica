package ar.utn.sistema.entities.usuarios;

import ar.utn.sistema.entities.PersistenciaID;
import ar.utn.sistema.entities.notificacion.Contacto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class Tecnico extends Rol {
    private String nombre;
    private String apellido;
    private TipoDocumento tipoDocumento;
    private Long documento;
    private Long cuil;
    private Contacto contacto;
    private String areaCobertura;

    public Tecnico(String nombre, String apellido, TipoDocumento tipoDocumento, Long documento, Long cuil, Contacto contacto, String areaCobertura) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.tipoDocumento = tipoDocumento;
        this.documento = documento;
        this.cuil = cuil;
        this.contacto = contacto;
        this.areaCobertura = areaCobertura;
    }
}

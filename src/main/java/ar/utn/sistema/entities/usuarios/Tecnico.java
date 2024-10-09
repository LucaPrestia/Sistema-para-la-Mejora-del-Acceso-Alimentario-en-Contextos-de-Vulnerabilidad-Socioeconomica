package ar.utn.sistema.entities.usuarios;

import ar.utn.sistema.entities.Direccion;
import ar.utn.sistema.entities.PersistenciaID;
import ar.utn.sistema.entities.notificacion.Contacto;
import ar.utn.sistema.entities.notificacion.Notificacion;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class Tecnico extends Rol {
    private Usuario usuario;
    private String nombre;
    private String apellido;
    private TipoDocumento tipoDocumento;
    private Long documento;
    private Long cuil;
    private Contacto contacto;
    private String areaCobertura;

    // agregamos la dirección para que se pueda identificar al técnico más cercano a una determinada heladera en caso de detectar algún incidente
    private Direccion direccion;

    public Tecnico(Usuario usuario, String nombre, String apellido, TipoDocumento tipoDocumento, Long documento, Long cuil, Contacto contacto, String areaCobertura, Direccion direccion) {
        this.usuario = usuario;
        this.nombre = nombre;
        this.apellido = apellido;
        this.tipoDocumento = tipoDocumento;
        this.documento = documento;
        this.cuil = cuil;
        this.contacto = contacto;
        this.areaCobertura = areaCobertura;
        this.direccion = direccion;
    }

    public void notificar(Notificacion notificacion) {
        notificacion.setContacto(contacto.getContacto());
        notificacion.setUsuario(this.usuario);
        contacto.notificar(notificacion);
    }
}

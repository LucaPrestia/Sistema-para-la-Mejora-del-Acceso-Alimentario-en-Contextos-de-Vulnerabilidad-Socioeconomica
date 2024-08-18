package ar.utn.sistema.entities.usuarios;

import ar.utn.sistema.entities.PersistenciaID;
import ar.utn.sistema.entities.colaboracion.Colaboracion;
import ar.utn.sistema.entities.notificacion.Contacto;
import ar.utn.sistema.entities.notificacion.Notificacion;
import ar.utn.sistema.entities.Direccion;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter @Setter @NoArgsConstructor
public abstract class Colaborador extends PersistenciaID {
    private Usuario usuario; // información de usuario en una entidad aparte
    private List<Contacto> contactos = new ArrayList<Contacto>();
    private Direccion direccion;
    private List<Colaboracion> colaboraciones = new ArrayList<Colaboracion>();

    public void agregarColaboracion(Colaboracion colaboracion){
        this.colaboraciones.add(colaboracion);
    }
    public void agregarContacto(Contacto contacto) {this.contactos.add(contacto);}
    public void notificar(Notificacion notificacion) {
        for (Contacto contacto : contactos) {
            if (contacto.getMedio() == notificacion.getMedio()) {
                notificacion.setContacto(contacto.getContacto());
                contacto.notificar(notificacion);
                break;
            }
        }
    }
}

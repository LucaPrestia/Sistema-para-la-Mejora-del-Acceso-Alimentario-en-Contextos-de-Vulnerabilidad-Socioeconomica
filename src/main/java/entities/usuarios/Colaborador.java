package entities.usuarios;

import entities.Direccion;
import entities.PersistenciaID;
import entities.colaboracion.Colaboracion;
import entities.notificacion.Contacto;
import entities.notificacion.Notificacion;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter @Setter @NoArgsConstructor
public abstract class Colaborador extends PersistenciaID {

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

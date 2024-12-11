package ar.utn.sistema.entities.usuarios;

import ar.utn.sistema.entities.notificacion.Notificacion;
import ar.utn.sistema.entities.notificacion.PreferenciaNotificacion;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Suscriptor extends Rol{
    public void notificar(Notificacion notificacion) {

    }

    public boolean correspondeVerificar(PreferenciaNotificacion preferencia, int cantidadViandas) {
        return false;
    }
}

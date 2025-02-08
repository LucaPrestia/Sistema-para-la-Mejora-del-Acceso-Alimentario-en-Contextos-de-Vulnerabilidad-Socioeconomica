package ar.utn.sistema.entities.usuarios;

import ar.utn.sistema.entities.notificacion.Notificacion;
import ar.utn.sistema.entities.notificacion.PreferenciaNotificacion;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.MappedSuperclass;

import java.io.IOException;

@MappedSuperclass
public abstract class Suscriptor extends Rol{
    public void notificar(Notificacion notificacion) throws IOException {

    }

    public boolean correspondeVerificar(PreferenciaNotificacion preferencia, int cantidadViandas) {
        return false;
    }
}

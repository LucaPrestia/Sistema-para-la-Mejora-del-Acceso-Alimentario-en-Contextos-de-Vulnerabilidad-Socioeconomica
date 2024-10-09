package ar.utn.sistema.entities.usuarios;

import ar.utn.sistema.entities.notificacion.Notificacion;
import ar.utn.sistema.entities.notificacion.PreferenciaNotificacion;

public interface Suscriptor {
    void notificar(Notificacion notificacion);
    boolean correspondeVerificar(PreferenciaNotificacion preferencia, int cantidadViandas);
}

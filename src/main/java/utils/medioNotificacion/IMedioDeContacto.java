package utils.medioNotificacion;

import entities.notificacion.Notificacion;

public interface IMedioDeContacto {
    public void notificar(Notificacion notificacion);
}

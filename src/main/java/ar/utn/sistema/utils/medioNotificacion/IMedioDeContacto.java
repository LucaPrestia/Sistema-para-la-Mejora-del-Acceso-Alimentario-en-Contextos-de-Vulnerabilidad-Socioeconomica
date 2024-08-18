package ar.utn.sistema.utils.medioNotificacion;

import ar.utn.sistema.entities.notificacion.Notificacion;

public interface IMedioDeContacto {
    public void notificar(Notificacion notificacion);
}

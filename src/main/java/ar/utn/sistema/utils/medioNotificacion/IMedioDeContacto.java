package ar.utn.sistema.utils.medioNotificacion;

import ar.utn.sistema.entities.notificacion.Notificacion;

import java.io.IOException;

public interface IMedioDeContacto {
    public void notificar(Notificacion notificacion) throws IOException;
}

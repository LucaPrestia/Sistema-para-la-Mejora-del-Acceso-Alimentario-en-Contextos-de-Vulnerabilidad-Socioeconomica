package ar.utn.sistema.utils.medioNotificacion.adapters;

import ar.utn.sistema.entities.notificacion.Notificacion;

import java.io.IOException;

public interface Adapter {
    public void enviar(Notificacion notificacion) throws IOException;
}

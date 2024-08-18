package ar.utn.sistema.utils.medioNotificacion.adapters;

import ar.utn.sistema.entities.notificacion.Notificacion;

public interface Adapter {
    public void enviar(Notificacion notificacion);
}

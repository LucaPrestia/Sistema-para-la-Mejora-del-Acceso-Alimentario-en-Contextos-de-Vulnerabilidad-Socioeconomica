package ar.utn.sistema.utils.medioNotificacion.adapters;

import ar.utn.sistema.entities.notificacion.Notificacion;

public class AdapterEmail implements Adapter{
    @Override
    public void enviar(Notificacion notificacion) {
        // TODO: integracion contra el servicio de mails
    }
}

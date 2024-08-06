package utils.medioNotificacion.adapters;

import entities.notificacion.Notificacion;

public class AdapterEmail implements Adapter{
    @Override
    public void enviar(Notificacion notificacion) {
        // TODO: integracion contra el servicio de mails
    }
}

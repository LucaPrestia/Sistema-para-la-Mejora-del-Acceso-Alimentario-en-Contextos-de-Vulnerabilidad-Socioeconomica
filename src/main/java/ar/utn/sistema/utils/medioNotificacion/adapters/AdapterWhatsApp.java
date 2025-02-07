package ar.utn.sistema.utils.medioNotificacion.adapters;

import ar.utn.sistema.entities.notificacion.Notificacion;
import ar.utn.sistema.services.SendWpp;

public class AdapterWhatsApp implements Adapter {
    @Override
    public void enviar(Notificacion notificacion) {
        SendWpp sendWpp = new SendWpp();
        sendWpp.enviarWpp(notificacion.getContacto(), notificacion.getMensaje());
    }
}

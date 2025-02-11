package ar.utn.sistema.utils.medioNotificacion.adapters;

import ar.utn.sistema.entities.notificacion.Notificacion;
import ar.utn.sistema.services.SendWpp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AdapterWhatsApp implements Adapter {
    @Autowired
    private SendWpp sendWpp;
    @Override
    public void enviar(Notificacion notificacion) {
        sendWpp.enviarWpp(notificacion.getContacto(), notificacion.getMensaje());
    }
}

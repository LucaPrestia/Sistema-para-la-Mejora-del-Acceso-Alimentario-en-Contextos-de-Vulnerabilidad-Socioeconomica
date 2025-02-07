package ar.utn.sistema.utils.medioNotificacion.adapters;

import ar.utn.sistema.entities.notificacion.Notificacion;
import ar.utn.sistema.services.TwilioEmailService;

public class AdapterEmail implements Adapter{
    @Override
    public void enviar(Notificacion notificacion) {
        TwilioEmailService twilioEmailService = new TwilioEmailService();
        twilioEmailService.enviarEmail(notificacion.getContacto(), "Notificacion Sistema Alimentario", notificacion.getMensaje());
    }
}

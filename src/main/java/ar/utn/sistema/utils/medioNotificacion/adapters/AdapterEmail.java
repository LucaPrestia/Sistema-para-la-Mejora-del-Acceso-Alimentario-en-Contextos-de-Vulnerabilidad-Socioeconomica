package ar.utn.sistema.utils.medioNotificacion.adapters;

import ar.utn.sistema.entities.notificacion.Notificacion;
import ar.utn.sistema.services.TwilioEmailService;

import java.io.IOException;

public class AdapterEmail implements Adapter{
    @Override
    public void enviar(Notificacion notificacion) throws IOException {
        TwilioEmailService twilioEmailService = new TwilioEmailService();
        System.out.println("se mando");
        twilioEmailService.enviarEmail(notificacion.getContacto(), "Notificacion Sistema Alimentario", notificacion.getMensaje());
    }
}

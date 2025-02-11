package ar.utn.sistema.utils.medioNotificacion.adapters;

import ar.utn.sistema.entities.notificacion.Notificacion;
import ar.utn.sistema.services.TwilioEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AdapterEmail implements Adapter{
    @Autowired
    private TwilioEmailService twilioEmailService;
    @Override
    public void enviar(Notificacion notificacion) throws IOException {
        System.out.println("se mando");
        twilioEmailService.enviarEmail(notificacion.getContacto(), "Notificacion Sistema Alimentario", notificacion.getMensaje());
    }
}

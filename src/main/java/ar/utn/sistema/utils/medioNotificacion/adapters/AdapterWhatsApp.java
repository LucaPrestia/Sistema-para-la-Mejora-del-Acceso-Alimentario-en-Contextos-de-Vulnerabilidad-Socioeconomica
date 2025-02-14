package ar.utn.sistema.utils.medioNotificacion.adapters;

import ar.utn.sistema.entities.notificacion.Notificacion;
import ar.utn.sistema.services.SendWpp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AdapterWhatsApp implements Adapter {

    @Override
    public void enviar(Notificacion notificacion) throws IOException {
        System.out.println("llege al adapter");

        SendWpp.instancia().enviarWpp(notificacion.getContacto(), notificacion.getMensaje());
    }
}

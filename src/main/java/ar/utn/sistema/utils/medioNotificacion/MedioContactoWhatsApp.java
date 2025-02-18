package ar.utn.sistema.utils.medioNotificacion;

import ar.utn.sistema.entities.notificacion.Notificacion;
import lombok.Getter;
import lombok.Setter;
import ar.utn.sistema.utils.medioNotificacion.adapters.AdapterWhatsApp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Getter @Setter @Component
public class MedioContactoWhatsApp implements IMedioDeContacto{
    private AdapterWhatsApp adapter;
    @Autowired
    public MedioContactoWhatsApp(AdapterWhatsApp adapter) {
        this.adapter = adapter;
    }

    @Override
    public void notificar(Notificacion notificacion) throws IOException {
        System.out.println("llege al mediocontecto");

        this.adapter.enviar(notificacion);
    }
}

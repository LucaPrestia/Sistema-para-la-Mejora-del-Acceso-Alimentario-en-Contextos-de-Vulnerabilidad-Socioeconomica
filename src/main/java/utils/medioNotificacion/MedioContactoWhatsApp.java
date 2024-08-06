package utils.medioNotificacion;

import entities.notificacion.Notificacion;
import lombok.Getter;
import lombok.Setter;
import utils.medioNotificacion.adapters.AdapterWhatsApp;

@Getter @Setter
public class MedioContactoWhatsApp implements IMedioDeContacto{
    private AdapterWhatsApp adapter;

    public MedioContactoWhatsApp(AdapterWhatsApp adapter) {
        this.adapter = adapter;
    }

    @Override
    public void notificar(Notificacion notificacion) {
        this.adapter.enviar(notificacion);
    }
}

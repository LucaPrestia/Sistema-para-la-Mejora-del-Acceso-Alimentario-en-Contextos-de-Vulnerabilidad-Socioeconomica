package ar.utn.sistema.utils.medioNotificacion;

import ar.utn.sistema.entities.notificacion.Notificacion;
import lombok.Getter;
import lombok.Setter;
import ar.utn.sistema.utils.medioNotificacion.adapters.AdapterWhatsApp;

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

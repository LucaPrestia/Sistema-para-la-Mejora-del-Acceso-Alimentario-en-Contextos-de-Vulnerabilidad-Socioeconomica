package utils.medioNotificacion;

import entities.notificacion.Notificacion;
import lombok.Getter;
import lombok.Setter;
import utils.medioNotificacion.adapters.AdapterEmail;

@Getter @Setter
public class MedioContactoEmail implements IMedioDeContacto{
    private AdapterEmail adapter;

    public MedioContactoEmail(AdapterEmail adapter) {
        this.adapter = adapter;
    }

    @Override
    public void notificar(Notificacion notificacion) {
        this.adapter.enviar(notificacion);
    }
}

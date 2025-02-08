package ar.utn.sistema.utils.medioNotificacion;

import ar.utn.sistema.entities.notificacion.Notificacion;
import ar.utn.sistema.utils.medioNotificacion.adapters.AdapterEmail;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

@Getter @Setter
public class MedioContactoEmail implements IMedioDeContacto{
    private AdapterEmail adapter;

    public MedioContactoEmail(AdapterEmail adapter) {
        this.adapter = adapter;
    }

    @Override
    public void notificar(Notificacion notificacion) throws IOException {
        this.adapter.enviar(notificacion);
    }
}

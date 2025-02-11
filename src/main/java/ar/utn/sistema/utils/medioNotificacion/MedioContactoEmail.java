package ar.utn.sistema.utils.medioNotificacion;

import ar.utn.sistema.entities.notificacion.Notificacion;
import ar.utn.sistema.utils.medioNotificacion.adapters.AdapterEmail;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Getter @Setter @Component
public class MedioContactoEmail implements IMedioDeContacto{
    private AdapterEmail adapter;

    @Autowired
    public MedioContactoEmail(AdapterEmail adapter) {
        this.adapter = adapter;
    }

    @Override
    public void notificar(Notificacion notificacion) throws IOException {
        this.adapter.enviar(notificacion);
    }
}

package ar.utn.sistema.entities.notificacion;

import ar.utn.sistema.entities.PersistenciaID;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ar.utn.sistema.utils.medioNotificacion.IMedioDeContacto;
import ar.utn.sistema.utils.medioNotificacion.MedioContactoEmail;
import ar.utn.sistema.utils.medioNotificacion.MedioContactoWhatsApp;
import ar.utn.sistema.utils.medioNotificacion.adapters.AdapterEmail;
import ar.utn.sistema.utils.medioNotificacion.adapters.AdapterWhatsApp;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

@Entity
@Getter @Setter @NoArgsConstructor
public class Contacto extends PersistenciaID {
    @Enumerated(EnumType.STRING)
    private MedioNotificacion medio;
    private String contacto;

    @Transient
    private IMedioDeContacto medioDeContacto;
    public Contacto(MedioNotificacion medio, String contacto) {
        this.medio = medio;
        this.contacto = contacto;
    }
    public void notificar(Notificacion notificacion) throws IOException {
        if (medioDeContacto != null) {
            medioDeContacto.notificar(notificacion);
        }
    }

}

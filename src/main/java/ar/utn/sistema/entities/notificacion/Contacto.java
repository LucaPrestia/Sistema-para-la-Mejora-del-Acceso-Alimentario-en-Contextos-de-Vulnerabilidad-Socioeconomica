package ar.utn.sistema.entities.notificacion;

import ar.utn.sistema.entities.PersistenciaID;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ar.utn.sistema.utils.medioNotificacion.IMedioDeContacto;
import ar.utn.sistema.utils.medioNotificacion.MedioContactoEmail;
import ar.utn.sistema.utils.medioNotificacion.MedioContactoWhatsApp;
import ar.utn.sistema.utils.medioNotificacion.adapters.AdapterEmail;
import ar.utn.sistema.utils.medioNotificacion.adapters.AdapterWhatsApp;
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

    public void notificar(Notificacion notificacion) {
        medioDeContacto.notificar(notificacion);
    }

    // @PostLoad
    public void postLoad() {
        initializarMedioDeContacto();
    }

    // @PostUpdate
    public void postUpdate() {
        initializarMedioDeContacto();
    }

    public void initializarMedioDeContacto() {
        switch (this.medio) {
            case EMAIL:
                this.medioDeContacto = new MedioContactoEmail(new AdapterEmail());
                break;
            case TELEFONO:
                // Inicializar medioDeContacto para teléfono
                break;
            case WHATSAPP:
                this.medioDeContacto = new MedioContactoWhatsApp(new AdapterWhatsApp());
                break;
        }
    }
}

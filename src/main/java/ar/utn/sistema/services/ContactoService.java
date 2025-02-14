package ar.utn.sistema.services;

import ar.utn.sistema.entities.notificacion.Contacto;
import ar.utn.sistema.utils.medioNotificacion.MedioContactoEmail;
import ar.utn.sistema.utils.medioNotificacion.MedioContactoWhatsApp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactoService {

    @Autowired
    private MedioContactoEmail medioContactoEmail;

    @Autowired
    private MedioContactoWhatsApp medioContactoWhatsApp;



    public void inicializarMediosDeContacto(List<Contacto> contactos) {
        for (Contacto contacto : contactos) {
            switch (contacto.getMedio()) {
                case EMAIL:
                    System.out.println("meti mail");
                    contacto.setMedioDeContacto(medioContactoEmail);
                    break;
                case TELEFONO:
                case WHATSAPP:
                    System.out.println("meti wp");
                    contacto.setMedioDeContacto(medioContactoWhatsApp);
                    break;
            }
        }

    }


}
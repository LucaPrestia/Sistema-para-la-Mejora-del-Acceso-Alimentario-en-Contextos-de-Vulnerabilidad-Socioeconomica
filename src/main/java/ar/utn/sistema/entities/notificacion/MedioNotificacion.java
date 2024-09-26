package ar.utn.sistema.entities.notificacion;

import lombok.Getter;

@Getter
public enum MedioNotificacion {
    EMAIL("Email"),
    TELEFONO("Teléfono"),
    WHATSAPP("WhatsApp");

    private final String value;

    MedioNotificacion(String value){this.value = value;}
}

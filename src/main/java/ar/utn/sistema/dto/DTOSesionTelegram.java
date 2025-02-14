package ar.utn.sistema.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter@Setter@NoArgsConstructor
public class DTOSesionTelegram {
    public long id_telegram;
    public int id_usuario;
    public long chat_id;

    public DTOSesionTelegram(long id_telegram, int id_usuario, long chat_id) {
        this.id_telegram = id_telegram;
        this.id_usuario = id_usuario;
        this.chat_id = chat_id;

    }
}

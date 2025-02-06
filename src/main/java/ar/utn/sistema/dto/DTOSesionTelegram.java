package ar.utn.sistema.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter@Setter@NoArgsConstructor
public class DTOSesionTelegram {
    public long id_telegram;
    public int id_usuario;
    public long chat_id;
}

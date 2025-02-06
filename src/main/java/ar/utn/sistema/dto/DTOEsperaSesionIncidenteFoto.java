package ar.utn.sistema.dto;

import ar.utn.sistema.entities.incidente.Incidente;
import ar.utn.sistema.entities.usuarios.Tecnico;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter@Setter@NoArgsConstructor
public class DTOEsperaSesionIncidenteFoto {
    public long id_telegram;
    public Incidente incidente;
    public Boolean solucionado;
    public String descripcion;
    public long chat_id;
    public Tecnico tecnico;
}

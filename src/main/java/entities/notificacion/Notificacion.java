package entities.notificacion;

import entities.PersistenciaID;
import entities.usuarios.Colaborador;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Notificacion extends PersistenciaID {
    // lo puse como persistencia porque en la entrega 3 dice que hay que persistir las notificaciones
    private String mensaje;
    private Colaborador destinatario;
    private MedioNotificacion medio;
    private String contacto;
}

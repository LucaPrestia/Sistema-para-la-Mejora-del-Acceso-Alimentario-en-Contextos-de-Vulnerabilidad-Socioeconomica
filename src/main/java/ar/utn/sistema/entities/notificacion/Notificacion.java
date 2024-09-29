package ar.utn.sistema.entities.notificacion;

import ar.utn.sistema.entities.PersistenciaID;
import ar.utn.sistema.entities.usuarios.Colaborador;
import ar.utn.sistema.entities.usuarios.Usuario;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Getter @Setter
public class Notificacion extends PersistenciaID {
    // lo puse como persistencia porque en la entrega 3 dice que hay que persistir las notificaciones
    private Usuario usuario;
    private String mensaje;
    private MedioNotificacion medio;
    private String contacto;
    private LocalDateTime fechaHora;

    public Notificacion(String mensaje, MedioNotificacion medio) {
        this.mensaje = mensaje;
        this.medio = medio;
        this.fechaHora = LocalDateTime.now();
    }

    public Notificacion(String mensaje) {
        this.mensaje = mensaje;
        this.fechaHora = LocalDateTime.now();
    }
}

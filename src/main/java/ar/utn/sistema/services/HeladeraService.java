package ar.utn.sistema.services;

import ar.utn.sistema.entities.heladera.EstadoHeladera;
import ar.utn.sistema.entities.heladera.Heladera;
import ar.utn.sistema.entities.incidente.Incidente;
import ar.utn.sistema.entities.incidente.IncidenteAlerta;
import ar.utn.sistema.entities.notificacion.Notificacion;
import ar.utn.sistema.model.MensajeTemperatura;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class HeladeraService {
    public List<Heladera> obtenerTodasLasHeladeras() {
        // TODO: traer heladeras de la base (repo)
        Heladera h = new Heladera();
        List<Heladera> l = new ArrayList<Heladera>();
        l.add(h);
        return l;
    }

    public Heladera obtenerHeladeraPorId(Integer heladeraId) {
        // TODO: traer heladera de la base (repo)
        return new Heladera();
    }

    // ----------- RECIBIR MENSAJES SENSORES BROKER -------------
    // https://keepcoding.io/blog/que-es-rabbitmq-y-como-funciona/
    // sensor temperatura
    // @RabbitListener(queues = "temperatura.heladera")
    public void registrarTemperatura(MensajeTemperatura mensaje) {
        Heladera heladera = obtenerHeladeraPorId(mensaje.getHeladeraId());
        double temperatura = mensaje.getTemperatura();
        heladera.setUltTempRegs(temperatura);
        if(temperatura < heladera.getTempMin() || temperatura > heladera.getTempMax()){
            String temperaturaStr = temperatura < heladera.getTempMin() ? "baja" : "alta";
            String mensajeNotificacion = "Alerta de temperatura " + temperaturaStr +
                    "( " + temperatura + "°C)" + " en la heladera '" + heladera.getNombre() +
                    "' ubicada en la dirección " + heladera.getDireccion().obtenerCadenaDireccion() + ".";
            registrarAlerta(heladera, "TEMPERATURA", mensajeNotificacion);
        } else {
            // todo: persistir heladera
        }
    }

    // sensor movimiento
    // @RabbitListener(queues = "movimiento.heladera", priority = "10") // mayor prioridad
    public void registrarMovimiento(Integer heladeraId) {
        Heladera heladera = obtenerHeladeraPorId(heladeraId);
        String mensajeNotificacion = "Alerta de movimiento, posible fraude en la heladera '" + heladera.getNombre() +
                "' ubicada en la dirección " + heladera.getDireccion().obtenerCadenaDireccion() + ".";
        registrarAlerta(heladera, "FRAUDE", mensajeNotificacion);
    }

    private void registrarAlerta(Heladera heladera, String motivo, String mensajeNotif){
        heladera.setEstado(EstadoHeladera.INACTIVA);
        Incidente incidente = new IncidenteAlerta(LocalDateTime.now(), heladera, motivo);
        Notificacion notificacion = new Notificacion(mensajeNotif);
        incidente.notificarTecnico(notificacion);
        heladera.notificarDesperfecto(notificacion);
        // todo: persistir heladera + incidente + notificacion
    }

    // todo: analizar cómo hacer alerta conexion!
    // todo: analizar como recibir autorizacion de apertura!
}

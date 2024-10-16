package ar.utn.sistema.services;

import ar.utn.sistema.entities.heladera.EstadoHeladera;
import ar.utn.sistema.entities.heladera.Heladera;
import ar.utn.sistema.entities.incidente.Incidente;
import ar.utn.sistema.entities.incidente.IncidenteAlerta;
import ar.utn.sistema.entities.notificacion.Notificacion;
import ar.utn.sistema.model.MensajeTemperatura;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class HeladeraService {

    public List<Heladera> obtenerTodasLasHeladeras() {
        // TODO: fetch heladeras from the database (repo)
        Heladera h = new Heladera();
        List<Heladera> l = new ArrayList<>();
        l.add(h);
        return l;
    }

    public Heladera obtenerHeladeraPorId(Integer heladeraId) {
        // TODO: fetch heladera from the database (repo)
        return new Heladera();
    }

    // Receive temperature sensor messages
    @RabbitListener(queues = "temperatura.heladera")
    public void registrarTemperatura(MensajeTemperatura mensaje) {
        Heladera heladera = obtenerHeladeraPorId(mensaje.getHeladeraId());
        double temperatura = mensaje.getTemperatura();
        heladera.setUltTempRegs(temperatura);

        if (temperatura < heladera.getTempMin() || temperatura > heladera.getTempMax()) {
            String temperaturaStr = (temperatura < heladera.getTempMin()) ? "baja" : "alta";
            String mensajeNotificacion = "Alerta de temperatura " + temperaturaStr +
                    " ( " + temperatura + "°C) en la heladera '" + heladera.getNombre() +
                    "' ubicada en la dirección " + heladera.getDireccion().obtenerCadenaDireccion() + ".";
            registrarAlerta(heladera, "TEMPERATURA", mensajeNotificacion);
        } else {
            // TODO: persist heladera state
        }
    }

    // Receive movement sensor messages
    @RabbitListener(queues = "movimiento.heladera", priority = "10") // higher priority
    public void registrarMovimiento(Integer heladeraId) {
        Heladera heladera = obtenerHeladeraPorId(heladeraId);
        String mensajeNotificacion = "Alerta de movimiento, posible fraude en la heladera '" + heladera.getNombre() +
                "' ubicada en la dirección " + heladera.getDireccion().obtenerCadenaDireccion() + ".";
        registrarAlerta(heladera, "FRAUDE", mensajeNotificacion);
    }

    private void registrarAlerta(Heladera heladera, String motivo, String mensajeNotif) {
        heladera.setEstado(EstadoHeladera.INACTIVA);
        Incidente incidente = new IncidenteAlerta(LocalDateTime.now(), heladera, motivo);
        Notificacion notificacion = new Notificacion(mensajeNotif);
        incidente.notificarTecnico(notificacion);
        heladera.notificarDesperfecto(notificacion);
        // TODO: persist heladera + incidente + notificacion
    }
}

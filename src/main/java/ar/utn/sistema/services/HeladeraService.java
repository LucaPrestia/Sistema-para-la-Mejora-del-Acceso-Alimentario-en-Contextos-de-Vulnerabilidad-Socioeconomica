package ar.utn.sistema.services;

import ar.utn.sistema.entities.heladera.EstadoHeladera;
import ar.utn.sistema.entities.heladera.Heladera;
import ar.utn.sistema.model.MensajeTemperatura;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

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
    @RabbitListener(queues = "temperatura.heladera")
    public void registrarTemperatura(MensajeTemperatura mensaje) {
        Heladera heladera = obtenerHeladeraPorId(mensaje.getHeladeraId());
        double temperatura = mensaje.getTemperatura();
        heladera.setUltTempRegs(temperatura);
        if(temperatura < heladera.getTempMin() || temperatura > heladera.getTempMax()){
            heladera.setEstado(EstadoHeladera.INACTIVA);
            // todo: continuar con los incidentes y las alertas!
        }
        // todo: persistir
    }

    // sensor movimiento
    @RabbitListener(queues = "movimiento.heladera", priority = "10") // mayor prioridad
    public void registrarMovimiento(Integer heladeraId) {
        Heladera heladera = obtenerHeladeraPorId(heladeraId);
        // todo: continuar con los incidentes y las alertas!
        // todo: persistir
    }
}

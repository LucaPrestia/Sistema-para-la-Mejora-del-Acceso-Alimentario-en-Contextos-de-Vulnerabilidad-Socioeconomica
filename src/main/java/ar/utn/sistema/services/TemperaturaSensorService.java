package ar.utn.sistema.services;

import ar.utn.sistema.entities.heladera.Heladera;
import ar.utn.sistema.model.MensajeTemperatura;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class TemperaturaSensorService {
    // https://keepcoding.io/blog/que-es-rabbitmq-y-como-funciona/
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private HeladeraService service;

    private static final Random random = new Random();

    @Scheduled(fixedRate = 300000) // cada 5 minutos
    public void enviarTemperaturas() {
        List<Heladera> heladeras = service.obtenerTodasLasHeladeras();
        for (Heladera heladera : heladeras) {
            // Se podría obtener la temperatura desde un sensor o una API externa
            double temperatura = obtenerTemperaturaDeHeladera(heladera);
            rabbitTemplate.convertAndSend("heladera.exchange", "temperatura.heladera", new MensajeTemperatura(heladera.getId(), temperatura));
        }
    }

    public static double obtenerTemperaturaDeHeladera(Heladera heladera) {
        // TODO: logica obtención temperatura de heladera acá
        double minExtendido = heladera.getTempMin() - 1;
        double maxExtendido = heladera.getTempMax() + 1;

        // genera un valor aleatorio entre (tempMin - 1) y (tempMax + 1)
        return minExtendido + (maxExtendido - minExtendido) * random.nextDouble();
    }

}

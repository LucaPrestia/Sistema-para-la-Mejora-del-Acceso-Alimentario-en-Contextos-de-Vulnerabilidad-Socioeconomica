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

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private HeladeraService service;

    private static final Random random = new Random();

    @Scheduled(fixedRate = 300000) // every 5 minutes
    public void enviarTemperaturas() {
        List<Heladera> heladeras = service.obtenerTodasLasHeladeras();
        for (Heladera heladera : heladeras) {
            double temperatura = obtenerTemperaturaDeHeladera(heladera);
            MensajeTemperatura mensaje = new MensajeTemperatura(heladera.getId(), temperatura);

            try {
                // rabbitTemplate.convertAndSend("heladera.exchange", "temperatura.heladera", mensaje);
                service.registrarTemperatura(mensaje);
                System.out.println("Enviando mensaje temperatura");
            } catch (Exception e) {
                System.err.println("Error al enviar mensaje temperatura: " + e.getMessage());
            }
        }
    }

    public static double obtenerTemperaturaDeHeladera(Heladera heladera) {
        double minExtendido = heladera.getTempMin() - 2;
        double maxExtendido = heladera.getTempMax() + 2;

        return minExtendido + (maxExtendido - minExtendido) * random.nextDouble();
    }
}

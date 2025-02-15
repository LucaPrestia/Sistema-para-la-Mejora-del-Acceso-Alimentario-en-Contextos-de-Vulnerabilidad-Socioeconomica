package ar.utn.sistema.services;

import ar.utn.sistema.entities.heladera.Heladera;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Service
public class FallaConexionSensorService {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private HeladeraService service;
    private static final Random random = new Random();
    private static final double PROBABILIDAD_MOVIMIENTO = 0.15; // 15% de probabilidad

    @Scheduled(fixedRate = 300000) // Cada 5 minutos
    public void enviarFallaConexion() throws IOException {
        List<Heladera> heladeras = service.obtenerTodasLasHeladeras();
        Collections.shuffle(heladeras);
        for (Heladera heladera : heladeras) {
            if (random.nextDouble() < PROBABILIDAD_MOVIMIENTO) {
                //rabbitTemplate.convertAndSend("heladera.exchange", "movimiento.heladera", id );
                try {
                    System.out.println("Enviando mensaje fallo conexion");
                    service.registrarFalloConexion(heladera.getId());
                } catch (Exception e) {
                    System.err.println("Error al enviar mensaje temperatura: " + e.getMessage());
                }
                break;
            }
        }
    }
}

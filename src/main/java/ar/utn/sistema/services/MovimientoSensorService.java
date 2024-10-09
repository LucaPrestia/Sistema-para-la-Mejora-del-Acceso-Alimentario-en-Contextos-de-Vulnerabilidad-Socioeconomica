package ar.utn.sistema.services;

//import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class MovimientoSensorService {
    // https://keepcoding.io/blog/que-es-rabbitmq-y-como-funciona/
    /*
    @Autowired
    private RabbitTemplate rabbitTemplate;
    */

    public void enviarMovimiento(Integer id) {
        // rabbitTemplate.convertAndSend("heladera.exchange", "movimiento.heladera", id );
        System.out.println("envio mensaje heladera en movimiento");
    }
}

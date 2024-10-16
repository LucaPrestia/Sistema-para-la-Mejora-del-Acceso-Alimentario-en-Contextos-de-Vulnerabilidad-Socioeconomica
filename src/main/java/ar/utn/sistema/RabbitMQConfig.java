package ar.utn.sistema;

import ar.utn.sistema.model.MensajeTemperatura;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class RabbitMQConfig {

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // Declaration of the queue "temperatura.heladera"
    @Bean
    public Queue queueTemperatura() {
        return new Queue("temperatura.heladera", true); // true = durable queue
    }

    // Declaration of the queue "movimiento.heladera"
    @Bean
    public Queue queueMovimiento() {
        return new Queue("movimiento.heladera", true); // true = durable queue
    }

    // Declaration of the exchange
    @Bean
    public TopicExchange exchange() {
        return new TopicExchange("heladera.exchange");
    }

    // Binding for the temperature queue
    @Bean
    public Binding bindingTemperatura(Queue queueTemperatura, TopicExchange exchange) {
        return BindingBuilder.bind(queueTemperatura).to(exchange).with("temperatura.heladera");
    }

    // Binding for the movement queue
    @Bean
    public Binding bindingMovimiento(Queue queueMovimiento, TopicExchange exchange) {
        return BindingBuilder.bind(queueMovimiento).to(exchange).with("movimiento.heladera");
    }
}

package br.com.store.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.store.library.domain.dto.event.EventMessage;

@Component
public class MessageProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void publish(EventMessage eventeMessage, String queueName) {
        rabbitTemplate.convertAndSend(queueName, toJson(eventeMessage));
    }

    private String toJson(Object objectJson) throws RuntimeException {

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(objectJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

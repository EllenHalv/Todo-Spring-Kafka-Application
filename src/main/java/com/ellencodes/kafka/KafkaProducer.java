package com.ellencodes.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import com.ellencodes.kafka.payload.Todo;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {
    private final KafkaTemplate<String, Todo> kafkaProducer;
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducer.class);

    public KafkaProducer(KafkaTemplate<String, Todo> kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    public void sendMessage(Todo todo) {
        //LOGGER.info(String.format("Message sent: %s", todo.toString()));

        Message<Todo> message = MessageBuilder.withPayload(
                        todo).setHeader(
                        KafkaHeaders.TOPIC, "ellencodesJson").
                build();

        kafkaProducer.send(message);
    }
}

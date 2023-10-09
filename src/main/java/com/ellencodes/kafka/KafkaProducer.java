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

    public KafkaProducer(KafkaTemplate<String, Todo> kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    public void sendMessage(Todo todo) {
        Message<Todo> message = MessageBuilder.withPayload(
                        todo).setHeader(
                        KafkaHeaders.TOPIC, "ellencodesJson").
                build();

        kafkaProducer.send(message);
    }

    public void deleteMessage(String id) {
        Message<String> message = MessageBuilder.withPayload(
                        id).setHeader(
                        KafkaHeaders.TOPIC, "ellencodesJsonDelete").
                build();

        kafkaProducer.send(message);
    }

    public void getAllMessage() {
        Message<Todo> message = MessageBuilder.withPayload(
                        new Todo()).setHeader(
                        KafkaHeaders.TOPIC, "ellencodesJsonGet").
                build();

        kafkaProducer.send(message);
    }

    public void getOneMessage(String id) {
        Message<String> message = MessageBuilder.withPayload(
                        id).setHeader(
                        KafkaHeaders.TOPIC, "ellencodesJsonGetOne").
                build();

        kafkaProducer.send(message);
    }
}

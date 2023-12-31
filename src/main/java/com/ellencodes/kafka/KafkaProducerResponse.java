package com.ellencodes.kafka;
import com.ellencodes.kafka.payload.Todo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

// This producer sends the ID of database-objects to KafkaConsumerResponse
@Service
public class KafkaProducerResponse {

    private final KafkaTemplate<String, Long> kafkaProducer;

    @Autowired
    public KafkaProducerResponse(KafkaTemplate<String, Long> kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    public void sendResponse(Long todoId) {
        Message<Long> message = MessageBuilder.withPayload(
                        todoId).setHeader(
                        KafkaHeaders.TOPIC, "ellencodesJsonResponse").
                build();

        kafkaProducer.send(message);
    }
}

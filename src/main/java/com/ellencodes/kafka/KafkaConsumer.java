package com.ellencodes.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.ellencodes.kafka.payload.Todo;

@Service
public class KafkaConsumer {

    @KafkaListener(
            topics = "ellencodesJson",
            groupId = "JsonGroup"
    )
    public void consume(Todo todo) {
        System.out.println("\nJson message recieved -> " + String.format(todo.toString()));
    }
}

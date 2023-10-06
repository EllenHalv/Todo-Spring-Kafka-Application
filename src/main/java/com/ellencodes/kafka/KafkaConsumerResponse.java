package com.ellencodes.kafka;

import com.ellencodes.client.Client;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerResponse {

        @KafkaListener(
                topics = "ellencodesJsonResponse",
                groupId = "ResponseGroup"
        )
        public void processResponse(Long todoId) {
                //send the response to Client
                System.out.println("kafkaConsumerResponse tog emot: " + todoId);
                Client.setCurrentTodoId(todoId);
        }
}

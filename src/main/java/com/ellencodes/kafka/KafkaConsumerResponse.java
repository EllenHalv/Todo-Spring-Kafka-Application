package com.ellencodes.kafka;

import com.ellencodes.appservice.AppService;
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
                AppService.setCurrentTodoId(todoId);
        }
}

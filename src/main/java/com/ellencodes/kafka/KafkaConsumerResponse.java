package com.ellencodes.kafka;

import com.ellencodes.appservice.AppService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

// This consumer handles the ID of database-objects sent from KafkaProducerResponse
@Service
public class KafkaConsumerResponse {
        @KafkaListener(
                topics = "ellencodesJsonResponse",
                groupId = "ResponseGroup"
        )
        public void setCurrentTodoId(Long todoId) {
                AppService.setCurrentTodoId(todoId);
        }
}
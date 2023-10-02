package com.ellencodes.controller;

import com.ellencodes.kafka.KafkaProducer;
import com.ellencodes.kafka.payload.Todo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/messages")
public class MessageController {
    private final KafkaProducer kafkaProducer;

    public MessageController(KafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    @PostMapping("/publish")
    public ResponseEntity<String> publish(@RequestBody Todo todo) {
        kafkaProducer.sendMessage(todo);
        return ResponseEntity.ok("Json Message send to Topic");
    }
}

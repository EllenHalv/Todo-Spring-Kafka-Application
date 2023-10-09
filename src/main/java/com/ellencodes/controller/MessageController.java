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
        return ResponseEntity.ok("sent publish message");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestBody String id) {
        kafkaProducer.deleteMessage(id);
        return ResponseEntity.ok("sent delete message");
    }

    @GetMapping("/get")
    public ResponseEntity<String> getAll() {
        kafkaProducer.getAllMessage();
        return ResponseEntity.ok("sent get all message");
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<String> getOne(@PathVariable String id) {
        kafkaProducer.getOneMessage(id);
        return ResponseEntity.ok("sent get one message");
    }
}

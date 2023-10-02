package com.ellencodes.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.ellencodes.kafka.payload.Todo;
import com.ellencodes.kafka.repository.TodoRepository;


@Service
public class KafkaConsumerDb {

    @Autowired
    private TodoRepository todoRepository;

    @KafkaListener(
            topics = "ellencodesJson",
            groupId = "JsonGroupDB"
    )
    public void writeToDb(Todo todo) {
        System.out.println(todo);
        System.out.println("Skickar data till DB!");
        //skicka datan till databasen
        todoRepository.save(todo);
    }
}

package com.ellencodes.kafka;

import com.ellencodes.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.ellencodes.kafka.payload.Todo;
import com.ellencodes.kafka.repository.TodoRepository;

import java.util.ArrayList;


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

    @KafkaListener(
            topics = "ellencodesJsonDelete",
            groupId = "JsonGroupDB"
    )
    public void deleteFromDb(String id) {
        System.out.println("Tar bort data från DB!");
        //ta bort datan från databasen
        todoRepository.delete(todoRepository.findById(Long.valueOf(id)).get());
    }

    @KafkaListener(
            topics = "ellencodesJsonGet",
            groupId = "JsonGroupDB"
    )
    public void getAllFromDb() {
        System.out.println("Hämtar data från DB!");

        //hämta datan från databasen
        ArrayList<Todo> todos = (ArrayList<Todo>) todoRepository.findAll();

        //skicka till en metod i client som kan hämtas av den andra metoden i client
        Client.setTodos(todos);
    }
}

package com.ellencodes.kafka;

import com.ellencodes.client.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.ellencodes.kafka.payload.Todo;
import com.ellencodes.kafka.repository.TodoRepository;

import java.util.ArrayList;
import java.util.Optional;


@Service
public class KafkaConsumerDb {

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private KafkaProducerResponse kafkaProducerResponse;

    @KafkaListener(
            topics = "ellencodesJson",
            groupId = "JsonGroupDB"
    )
    public void writeToDb(Todo todo) {
        System.out.println("Skickar data till DB!");
        //skicka datan till databasen
        Todo dbTodo = todoRepository.save(todo);

        //skicka ID till ellencodesJsonRespone topic
        kafkaProducerResponse.sendResponse(dbTodo.getId());
        System.out.println("Skickar ID till ellencodesJsonRespone topic"+ dbTodo);
    }

    @KafkaListener(
            topics = "ellencodesJsonDelete",
            groupId = "JsonGroupDB"
    )
    public void deleteFromDb(String id) {
        System.out.println("Tar bort data från DB!");

        // Try to find the Todo by ID
        Optional<Todo> todoOptional = todoRepository.findById(Long.valueOf(id));

        if (todoOptional.isPresent()) {
            System.out.println("Hittade todo med id: " + id);
            Todo todo = todoOptional.get();
            todoRepository.delete(todo);
        } else {
            System.out.println("Hittade inte todo med id: " + id);
        }
    }


    @KafkaListener(
            topics = "ellencodesJsonGet",
            groupId = "JsonGroupDB"
    )
    public void getAllFromDb() {
        System.out.println("Hämtar all data från DB!");

        //hämta datan från databasen
        ArrayList<Todo> todos = (ArrayList<Todo>) todoRepository.findAll();

        //skicka till en metod i clinet som sätter datan i en lista
        AppService.setTodos(todos);
    }

    @KafkaListener(
            topics = "ellencodesJsonGetOne",
            groupId = "JsonGroupDB"
    )
    public void getOneFromDb(String id) {
        System.out.println("Hämtar EN från DB!");

        //hämta datan från databasen
        Optional<Todo> todoOptional = todoRepository.findById(Long.valueOf(id));

        if (todoOptional.isPresent()) {
            System.out.println("Hittade todo med id: " + id);
            //skicka tillbaka att den hittades (returnera då id till gui)
            AppService.setCurrentTodoId(Long.valueOf(id));
        } else {
            System.out.println("Hittade inte todo med id: " + id);
            //skicka tillbaka att den inte hittades (returnera då null till gui)
            AppService.setCurrentTodoId(0L);
        }
    }
}

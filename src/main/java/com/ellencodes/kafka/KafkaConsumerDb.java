package com.ellencodes.kafka;

import com.ellencodes.appservice.AppService;
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
        Todo dbTodo = todoRepository.save(todo);

        kafkaProducerResponse.sendResponse(dbTodo.getId());
    }

    @KafkaListener(
            topics = "ellencodesJsonDelete",
            groupId = "JsonGroupDB"
    )
    public void deleteFromDb(String id) {
        Optional<Todo> todoOptional = todoRepository.findById(Long.valueOf(id));

        if (todoOptional.isPresent()) {
            Todo todo = todoOptional.get();
            todoRepository.delete(todo);
        }
    }


    @KafkaListener(
            topics = "ellencodesJsonGet",
            groupId = "JsonGroupDB"
    )
    public void getAllFromDb() {
        ArrayList<Todo> todos = (ArrayList<Todo>) todoRepository.findAll();

        AppService.setTodos(todos);
    }

    @KafkaListener(
            topics = "ellencodesJsonGetOne",
            groupId = "JsonGroupDB"
    )
    public void getOneFromDb(String id) {
        Optional<Todo> todoOptional = todoRepository.findById(Long.valueOf(id));

        if (todoOptional.isPresent()) {
            AppService.setCurrentTodoId(Long.valueOf(id));
        }
    }
}
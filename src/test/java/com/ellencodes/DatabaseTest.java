package com.ellencodes;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.ellencodes.kafka.repository.TodoRepository;
import com.ellencodes.kafka.payload.Todo;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = KafkaTodoApplication.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DatabaseTest {

    @Autowired
    TodoRepository todoRepository;

    static Todo testTodo;

    @BeforeEach
    void setUp() {
        System.out.println("Before Test");
    }

    @AfterEach
    void tearDown() {
        System.out.println("After Test");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("Alla test avslutade!");
    }

    @Test
    @Order(1)
    void createTodo() {
        //Skapa ett objekt av Todo med specifik data
        Todo todo = new Todo();
        todo.setTodoName("do dishes");

        //Spara todo till DB
        testTodo = todoRepository.save(todo);

        assertNotNull(todoRepository.findById(testTodo.getId()).get().getTodoName());

        System.out.println(testTodo.getId());
    }

    @Test
    @Order(2)
    void updateTodo() {
        //Hämta Todo med id 1
        Todo fetchedTodo = todoRepository.findById(testTodo.getId()).get();
        assertNotNull(fetchedTodo);

        //Updatera värdet i fetchedTodo
        fetchedTodo.setTodoName("clean house");
        todoRepository.save(fetchedTodo);
        assertEquals("clean house", todoRepository.findById(testTodo.getId()).get().getTodoName());
    }

    @Test
    @Order(3)
    void removeTodo() {
        //Kontrollera att todo med ID finns
        assertNotNull(todoRepository.findById(testTodo.getId()).get());

        //Ta bort todo med ID och kontroller att todo är borta
        todoRepository.deleteById(testTodo.getId());
        assertTrue(todoRepository.findById(testTodo.getId()).isEmpty());
    }
}

